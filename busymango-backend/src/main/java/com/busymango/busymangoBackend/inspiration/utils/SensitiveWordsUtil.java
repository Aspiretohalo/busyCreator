package com.busymango.busymangoBackend.inspiration.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SensitiveWordsUtil {
    public static final int MIN_MATCH_TYPE = 1;
    public static final int MAX_MATCH_TYPE = 2;

    public static Set<String> getSensitiveWord(String text, int matchType) {
        Set<String> sensitiveWords = new HashSet<>();
        int textLength = text.length();
        for (int i = 0; i < textLength; i++) {
            int length = matchSensitiveWordAtIndex(text, i, matchType);
            if (length > 0) {
                String sensitiveWord = text.substring(i, i + length);
                if (isCompleteSensitiveWord(sensitiveWord, matchType)) {
                    sensitiveWords.add(sensitiveWord);
                }
                i += length - 1;
            }
        }
        return sensitiveWords;
    }

    public static String replaceSensitiveWord(String text, int matchType, String replaceChar) {
        String result = text;
        Set<String> sensitiveWords = getSensitiveWord(text, matchType);
        for (String word : sensitiveWords) {
            String replaceString = getReplacementString(replaceChar, word.length());
            result = result.replaceAll(word, replaceString);
        }
        return result;
    }

    public static int checkSensitiveWord(String text, int matchType) {
        Set<String> sensitiveWords = getSensitiveWord(text, matchType);
        return sensitiveWords.size();
    }

    private static int matchSensitiveWordAtIndex(String text, int beginIndex, int matchType) {
        int matchFlag = 0;
        int textLength = text.length();
        TrieNode currentNode = SensitiveWordInit.sensitiveWordTrie;
        for (int i = beginIndex; i < textLength; i++) {
            char character = text.charAt(i);
            Map<Character, TrieNode> children = currentNode.getChildren();
            if (children != null && children.containsKey(character)) {
                matchFlag++;
                currentNode = children.get(character);
                if (currentNode.isEnd()) {
                    if (i < textLength - 1 && matchType == MAX_MATCH_TYPE) {
                        char nextChar = text.charAt(i + 1);
                        if (children.containsKey(nextChar)) {
                            continue;
                        }
                    }
                    break;
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || (matchType == MAX_MATCH_TYPE && matchFlag < textLength - beginIndex) || !currentNode.isEnd()) {
            matchFlag = 0;
        }
        return matchFlag;
    }

    private static boolean isCompleteSensitiveWord(String sensitiveWord, int matchType) {
        TrieNode currentNode = SensitiveWordInit.sensitiveWordTrie;
        int length = sensitiveWord.length();
        for (int i = 0; i < length; i++) {
            char character = sensitiveWord.charAt(i);
            Map<Character, TrieNode> children = currentNode.getChildren();
            if (children == null || !children.containsKey(character)) {
                return false;
            }
            currentNode = children.get(character);
            if (matchType == MAX_MATCH_TYPE && currentNode.isEnd()) {
                return false;
            }
        }
        return true;
    }

    private static String getReplacementString(String replaceChar, int length) {
        StringBuilder replacement = new StringBuilder(replaceChar);
        for (int i = 1; i < length; i++) {
            replacement.append(replaceChar);
        }
        return replacement.toString();
    }
}