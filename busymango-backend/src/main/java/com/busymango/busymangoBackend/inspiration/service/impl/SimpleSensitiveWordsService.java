package com.busymango.busymangoBackend.inspiration.service.impl;

import com.busymango.busymangoBackend.inspiration.service.ISensitiveWordsService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

public class SimpleSensitiveWordsService implements ISensitiveWordsService {

    @Override
    public Set<String> readSensitiveWords() {
        return Collections.emptySet(); // 返回一个空的不可变集合
    }

    @Override
    public LocalDateTime getLastCreatedAt() {
        return null;
    }
}
