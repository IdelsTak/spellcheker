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

class WordInfo implements Comparable<WordInfo> {

    private final String word;
    private final String meaning;

    WordInfo(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    /**
     * @return the word
     */
    String getWord() {
        return word;
    }

    /**
     * @return the meaning
     */
    String getMeaning() {
        return meaning;
    }

    @Override
    public String toString() {
        return word + ":" + meaning;
    }

    /**
     * Compares this {@code WordInfo} object with a specified {@code WordInfo}
     * object.
     * <p>
     * The comparison is alphabetical.
     *
     * @param otherInfo the specified {@code WordInfo} object that this
     * {@code WordInfo} is being compared to.
     *
     * @return {@code -1} if this {@code WordInfo} object’s word is
     * alphabetically less than {@code otherInfo}'s word. {@code 0} if this
     * {@code otherInfo} object’s word is the same as {@code otherInfo}'s word.
     * {@code 1} if if this {@code otherInfo} object’s word is alphabetically
     * greater {@code otherInfo}'s word.
     */
    @Override
    public int compareTo(WordInfo otherInfo) {
        if (this.word.compareTo(otherInfo.getWord()) < 0) {
            return -1;
        }
        if (this.word.compareTo(otherInfo.getWord()) > 0) {
            return 1;
        }
        return 0;
    }
}
