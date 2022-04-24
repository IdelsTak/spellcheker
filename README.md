# Object-Oriented Data Structures Using Java—Spell-checker



You are required to create a personal dictionary and spellcheck program. There must only be two core classes. A class `WordInfo` and a class `Dictionary`

The information stored on a `WordInfo` object consists of: 

- A word: a `String` 
- A meaning: a `String`

The `Dictionary` class is responsible for holding all the words and supports the following operations:

- `public boolean add (String word, String meaning)`—adds a new word to the dictionary . No duplicate words are allowed. You must store all the words using a binary search tree. All words should be stored in lowercase.
- `public boolean delete (String word)` —deletes the `WordInfo` object with the matching word.
- `public boolean exists(string word)`—returns `true` if the word is in the dictionary and `false` otherwise.
- `public String getMeaning(String word)`—returns the meaning of the word.
- `public int getCount()` —returns the number of words in the dictionary. Note that is must be implemented even if not used.
- `public String printWordList()` —returns a list of all the words stored in the dictionary in alphabetical order (only the words, not the meanings).
- `public void printDictionary()`- prints the full word and meaning for each `WordInfo` object in the dictionary (in ascending order).

Your program must first load all known words from the text file `wordlist.txt` into the dictionary class you created (which stores all the word as a binary tree). You must find a way to ensure that the initial binary tree is as close to a balanced tree as possible. 

>  NOTE that this list does not contain any definitions and none need to be created. Only new words need definitions. The words loaded from the file should have “Undefined word” as the definition.

Create a main program with the following menu options: 

1. Add new word
2. Delete word
3. Get meaning
4. Dictionary list
5. Spell check a text file
6. Exit

### Notes:

**Add new word**—This option should request a word and its meaning. Insert the word into the dictionary if it is new. No duplicates are allowed. All words are to be stored in lowercase letters.

**Delete word**—This option should request a word and delete it from the dictionary.

**Get meaning**—This option should request a word and print its meaning if found in the dictionary. If it is not found an appropriate message must be shown.

**Dictionary List**—This option must simply list all the words contained in the dictionary. Note that the meanings should not be listed.

**Spell check a text file**—This option should prompt the user for a text file. You may assume the text file contains only sentences with words and the only punctuation allowed are periods (.) , commas (,). You must print all the word that are not in the dictionary. Please note this must still work regardless of the case of the letters.

**Exit**—This option should exit the program.

>  ALL CLASSES MUST BE CREATED FROM SCRATCH. NO BUILT IN DATA STRUCTURES MUST BE USED.

