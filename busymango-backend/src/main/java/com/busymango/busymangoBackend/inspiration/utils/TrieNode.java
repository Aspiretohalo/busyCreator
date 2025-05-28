package com.busymango.busymangoBackend.inspiration.utils;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private Map<Character, TrieNode> children;
    private int isEnd;

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEnd = 0;
    }

    public TrieNode getChild(char c) {
        return children.get(c);
    }

    public TrieNode addChild(char c, int isEnd) {
        TrieNode childNode = children.get(c);
        if (childNode == null) {
            childNode = new TrieNode();
            children.put(c, childNode);
        }
        if (isEnd > childNode.isEnd) {
            childNode.isEnd = isEnd;
        }
        return childNode;
    }

    public Integer getEnd() {
        return isEnd;
    }

    public void setEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isEnd() {
        return isEnd > 0;
    }

    public boolean isLastChar() {
        return isEnd == 2;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }
}
