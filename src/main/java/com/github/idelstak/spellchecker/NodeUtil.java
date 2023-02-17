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

import com.github.idelstak.spellchecker.Dictionary.Node;
import java.util.Arrays;

class NodeUtil {

    private Node[] nodeArr = new Node[]{};

    Node[] toArr(Node root) {
        nodeArr = new Node[]{};
        inOrder(root);
        return Arrays.copyOf(nodeArr, nodeArr.length);
    }

    private void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft());
        Node[] ns = append(node);
        nodeArr = Arrays.copyOf(ns, ns.length);
        inOrder(node.getRight());
    }

    private Node[] append(Node n) {
        Node[] newArr = new Node[nodeArr.length + 1];
        for (int i = 0; i < nodeArr.length; i++) {
            newArr[i] = nodeArr[i];
        }
        newArr[nodeArr.length] = n;
        return newArr;
    }
}
