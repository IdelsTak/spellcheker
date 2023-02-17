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

import java.util.Arrays;
import java.util.Objects;

final class Dictionary {

    private Node root;
    private int size;

    Dictionary(String[] data) {
        for (String line : data) {
            if (line.contains(":")) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    add(parts[0].toLowerCase(), parts[1]);
                }
            } else {
                add(line.toLowerCase(), "Undefined word");
            }
        }
    }

    /**
     * Appends the specified values to this {@code Dictionary} object.
     * <p>
     * Uses the format {@code word};{@code meaning} while creating a new entry
     * for the specified values.
     *
     * @param word
     * @param meaning
     *
     * @return {@code true} if this {@code Dictionary} object changed as a
     * result of this method call; that is, the entry was actually inserted.
     * Otherwise, it returns {@code false}.
     */
    boolean add(String word, String meaning) {
        Node found = findNode(root, word);
        if (found != null) {
            if (!found.getInfo().getMeaning().trim().equalsIgnoreCase("undefined word")) {
                return false;
            } else {
                delete(word);
            }
        }
        WordInfo info = new WordInfo(word, meaning);
        if (root == null) {
            root = new Node(info, null);
            size++;
            return true;
        } else {
            Node node = root;
            int comp;
            while (true) {
                comp = info.compareTo(node.getInfo());
                if (comp == 0) {
                    return false;
                }
                if (comp < 0) {
                    if (node.getLeft() != null) {
                        node = node.getLeft();
                    } else {
                        node.setLeft(new Node(info, node));
                        size++;
                        return true;
                    }
                } else if (node.getRight() != null) {
                    node = node.getRight();
                } else {
                    node.setRight(new Node(info, node));
                    size++;
                    return true;
                }
            }
        }
    }

    /**
     * Removes the specified value from this {@code Dictionary} object.
     *
     * @param word the value whose absence will be ensured by this method call.
     *
     * @return {@code true} if this {@code Dictionary} object changed as a
     * result of this method call; that is, the entry was actually removed.
     * Otherwise, it returns {@code false}.
     */
    boolean delete(String word) {
        Node node = Objects.requireNonNull(findNode(root, word));
        size--;
        // If p has two children, replace p’s element with p’s successor’s
        // element, then make p reference that successor.
        if (node.getLeft() != null && node.getRight() != null) {
            Node successor = getSuccessor(node);
            node.setInfo(successor.getInfo());
            node = successor;
        } // p had two children
        // At this point, p has either no children or one child.
        Node replacement;
        if (node.getLeft() != null) {
            replacement = node.getLeft();
        } else {
            replacement = node.getRight();
        }
        // If p has at least one child, link replacement to p.parent.
        if (replacement != null) {
            replacement.setParent(node.getParent());
            if (node.getParent() == null) {
                root = replacement;
            } else if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(replacement);
            } else {
                node.getParent().setRight(replacement);
            }
        } // p has at least one child
        else if (node.getParent() == null) {
            root = null;
        } else {
            if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(null);
            } else {
                node.getParent().setRight(null);
            }
        } // p has a parent but no children
        return findNode(root, word) == null;
    }

    /**
     * Determines if there is a word in the {@code Dictionary} that equals the
     * specified {@code word}.
     *
     * @param word the value sought in this {@code Dictionary} object.
     *
     * @return if {@code true} there is an element in this {@code Dictionary}
     * object that equals the specified value; otherwise returns {@code false}.
     */
    boolean exists(String word) {
        return findNode(root, word) != null;
    }

    String getMeaning(String word) {
        Node node = findNode(root, word);
        return node == null ? null : node.getInfo().getMeaning();
    }

    /**
     * Returns the number of words in the dictionary.
     *
     *
     * @return the number of words in the dictionary
     */
    int getCount() {
        return size;
    }

    String printWordList() {
        StringBuilder sb = new StringBuilder();
        Node[] nodes = toArr(root);
        String[] words = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            words[i] = nodes[i].getInfo().getWord();
        }
        Arrays.sort(words);
        for (String word : words) {
            sb.append(word).append("\n");
        }
        return sb.toString();
    }

    void printDictionary() {
        Node[] nodes = toArr(root);
        WordInfo[] infos = new WordInfo[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            infos[i] = nodes[i].getInfo();
        }
        Arrays.sort(infos);
        for (WordInfo info : infos) {
            System.out.println(info.getWord());
        }
    }

    private Node findNode(Node root, String word) {
        if (root == null || root.getInfo().getWord().equalsIgnoreCase(word)) {
            return root;
        }
        if (word.toLowerCase().compareTo(root.getInfo().getWord().toLowerCase()) < 0) {
            return findNode(root.getLeft(), word);
        }
        return findNode(root.getRight(), word);
    }

    private Node[] toArr(Node root) {
        return new NodeUtil().toArr(root);
    }

    private Node getSuccessor(Node node) {
        if (node == null) {
            return null;
        } else if (node.getRight() != null) {
            // child is leftmost Node in right subtree of entry
            Node child = node.getRight();
            while (child.getLeft() != null) {
                child = child.getLeft();
            }
            return child;
        } // entry has a right child
        else {
            // go up the tree to the left as far as possible, then go up
            // to the right.
            Node parent = node.getParent();
            Node child = node;
            while (parent != null && child == parent.getRight()) {
                child = parent;
                parent = parent.getParent();
            }
            return parent;
        } // seems entry has no right child
    }

    static class Node {

        private WordInfo info;
        private Node parent;
        private Node left;
        private Node right;

        Node(WordInfo element, Node parent) {
            this.info = element;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "Node{" + "info=" + info + ", parent=" + parent + ", left=" + left + ", right=" + right + '}';
        }

        WordInfo getInfo() {
            return info;
        }

        void setInfo(WordInfo info) {
            this.info = info;
        }

        Node getParent() {
            return parent;
        }

        void setParent(Node parent) {
            this.parent = parent;
        }

        Node getLeft() {
            return left;
        }

        void setLeft(Node left) {
            this.left = left;
        }

        Node getRight() {
            return right;
        }

        void setRight(Node right) {
            this.right = right;
        }
    }
}
