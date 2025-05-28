package com.busymango.busymangoBackend.inspiration.utils;

import com.busymango.busymangoBackend.inspiration.service.ISensitiveWordsService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SensitiveWordInit {

    public static TrieNode sensitiveWordTrie;
    private LocalDateTime lastCreatedAt;
    private ISensitiveWordsService sensitiveWordsService;

    // 构造函数，接收服务对象
    public SensitiveWordInit(ISensitiveWordsService sensitiveWordsService) {
        this.sensitiveWordsService = sensitiveWordsService;
    }

    // 初始化敏感词
    public void initKeyWord() {
        try {
            Set<String> keyWordSet = sensitiveWordsService.readSensitiveWords();
            System.out.println("敏感词库数量：" + keyWordSet.size());
            sensitiveWordTrie = buildSensitiveWordTrie(keyWordSet);
            lastCreatedAt = sensitiveWordsService.getLastCreatedAt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 刷新敏感词
    public void refresh(boolean isForce) {
        if (isForce) {
            doRefresh();
            return;
        }
        LocalDateTime tempLastCreatedAt = sensitiveWordsService.getLastCreatedAt();
        if (Objects.isNull(tempLastCreatedAt) || Objects.isNull(lastCreatedAt) || tempLastCreatedAt.isAfter(lastCreatedAt)) {
            doRefresh();
        }
    }

    private void doRefresh() {
        try {
            System.out.println("开始刷新敏感词库");
            Set<String> keyWordSet = sensitiveWordsService.readSensitiveWords();
            System.out.println("敏感词库数量：" + keyWordSet.size());
            sensitiveWordTrie = buildSensitiveWordTrie(keyWordSet);
            lastCreatedAt = sensitiveWordsService.getLastCreatedAt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 构建敏感词前缀树
    private TrieNode buildSensitiveWordTrie(Set<String> keyWordSet) {
        TrieNode tempSensitiveWordTrie = new TrieNode();
        for (String word : keyWordSet) {
            TrieNode currentNode = tempSensitiveWordTrie;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                boolean isLastChar = (i == word.length() - 1);
                currentNode = currentNode.addChild(c, isLastChar ? 1 : 0);
            }
        }
        setLeafNodeEndFlag(tempSensitiveWordTrie.getChildren());
        return tempSensitiveWordTrie;
    }

    private void setLeafNodeEndFlag(Map<Character, TrieNode> children) {
        if (children == null) {
            return;
        }
        for (TrieNode childNode : children.values()) {
            setLeafNodeEndFlag(childNode.getChildren());
        }
        for (TrieNode childNode : children.values()) {
            if (childNode.isEnd() && childNode.getChildren().isEmpty()) {
                childNode.setEnd(2);
            }
        }
    }
}