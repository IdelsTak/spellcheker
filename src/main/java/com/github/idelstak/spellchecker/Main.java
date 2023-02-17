/*
 * The MIT License
 * Copyright © 2023 Hiram K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.idelstak.spellchecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = System.getProperty("user.dir") + File.separator + "Words.txt";
        String[] words = Files.lines(Paths.get(filePath)).toArray(String[]::new);
        Dictionary dictionary = new Dictionary(words);
        try ( Scanner scanner = new Scanner(System.in)) {
            showMenu(scanner, dictionary);
        }
    }

    private static void showMenu(Scanner scanner, Dictionary dictionary) throws FileNotFoundException {
        System.out.println("""
                           
                           =====================
                           SPELL CHEKER
                           =====================
                           1: Add new word
                           2: Delete word
                           3: Get meaning
                           4: Dictionary list
                           5: Spell check a text file.
                           6: Exit
                            """);
        System.out.print("Please select an action [1..6]> ");
        if (scanner.hasNextInt()) {
            processAction(dictionary, scanner, scanner.nextInt());
        } else {
            System.out.print("No action was selected. Continue (Y/N)? ");
            if (scanner.hasNext()) {
                if (scanner.next().equalsIgnoreCase("n")) {
                    doExit(dictionary, scanner);
                }
            }
        }
    }

    private static void doExit(Dictionary dictionary, Scanner scanner) throws FileNotFoundException {
        System.out.print("Are you sure you want to exit (Y/N)? ");
        if (scanner.hasNext()) {
            String in = scanner.next();
            if (in.equalsIgnoreCase("y")) {
                System.out.println("Bye!");
                System.exit(0);
            } else if (in.equalsIgnoreCase("n")) {
                showMenu(scanner, dictionary);
            }
        }
    }

    private static void processAction(Dictionary dictionary, Scanner scanner, int idx) throws FileNotFoundException {
        switch (idx) {
            case 1 -> {
                System.out.print("Please type a word and its meaning separated by \":\" [word:meaning] > ");
                if (scanner.hasNext()) {
                    String line = scanner.next();
                    String word = line;
                    String meaning = "Undefined word";
                    if (line.contains(":")) {
                        String[] parts = line.split(":");
                        if (parts.length > 0) {
                            word = parts[0];
                            meaning = parts[1];
                        }
                    }
                    System.out.print("Add \"%s\" with its meaning \"%s\" (Y/N)? ".formatted(word, meaning));
                    if (scanner.hasNext()) {
                        if (scanner.next().equalsIgnoreCase("y")) {
                            dictionary.add(word, meaning);
                            System.out.println("\"%s\" was added to dictionary".formatted(word));
                        }
                        showMenu(scanner, dictionary);
                    }
                }
            }
            case 2 -> {
                System.out.print("Please type the word you want deleted > ");
                if (scanner.hasNext()) {
                    String wordToDelete = scanner.next();
                    System.out.print("Are you sure you want to delete \"%s\" (Y/N)? ".formatted(wordToDelete));
                    if (scanner.hasNext()) {
                        if (scanner.next().equalsIgnoreCase("y")) {
                            boolean deleted = dictionary.delete(wordToDelete);
                            if (deleted) {
                                System.out.println("\"%s\" was deleted successfully".formatted(wordToDelete));
                            }
                        }
                        showMenu(scanner, dictionary);
                    }
                }
            }
            case 3 -> {
                System.out.print("Please type a word you want a meaning for > ");
                if (scanner.hasNext()) {
                    String needsMeaning = scanner.next();
                    String meaning = dictionary.getMeaning(needsMeaning.trim());
                    if (meaning != null) {
                        if (!meaning.isBlank() && !meaning.equalsIgnoreCase("undefined word")) {
                            System.out.println("\"%s\" means: \"%s\"".formatted(needsMeaning, meaning));
                        } else if (meaning.equalsIgnoreCase("undefined word")) {
                            System.out.println("\"%s\" is in the dictionary but has no meaning defined yet".formatted(needsMeaning));
                        }
                    } else {
                        System.out.println("\"%s\" hasn't been added to the dictionary yet".formatted(needsMeaning));
                    }
                    showMenu(scanner, dictionary);
                }
            }
            case 4 -> {
                System.out.print("List all the %d words in the dictionary (Y/N)? ".formatted(dictionary.getCount()));
                if (scanner.hasNext()) {
                    if (scanner.next().equalsIgnoreCase("y")) {
                        dictionary.printDictionary();
                    }
                    showMenu(scanner, dictionary);
                }
            }
            case 5 -> {
                System.out.print("Continue to select a file that will be checked for spelling (Y/N)? ");
                if (scanner.hasNext()) {
                    if (scanner.next().equalsIgnoreCase("y")) {
                        File wordsInputFile = getInputFileNameFromUser();
                        if (wordsInputFile == null) {
                            System.out.println("No file was selected.");
                        } else {
                            Scanner in = new Scanner(wordsInputFile);
                            in.useDelimiter("[^a-zA-Z]+");
                            String[] wordsNotInDictionary;
                            StringBuilder sb = new StringBuilder();
                            while (in.hasNext()) {
                                String wordFromFile = in.next();
                                if (!dictionary.exists(wordFromFile)) {
                                    sb.append(wordFromFile).append(";");
                                }
                            }
                            wordsNotInDictionary = sb.toString().split(";");
                            System.out.println("The following %d words from the file are not in the dictionary:".formatted(wordsNotInDictionary.length));
                            for (String wordNotInDictionary : wordsNotInDictionary) {
                                System.out.println(wordNotInDictionary);
                            }
                        }
                    }
                    showMenu(scanner, dictionary);
                }
            }
            case 6 ->
                doExit(dictionary, scanner);
            default ->
                throw new AssertionError();
        }
    }

    private static File getInputFileNameFromUser() {
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Select File for Input");
        int option = fileDialog.showOpenDialog(null);
        if (option != JFileChooser.APPROVE_OPTION) {
            return null;
        } else {
            return fileDialog.getSelectedFile();
        }
    }
}
