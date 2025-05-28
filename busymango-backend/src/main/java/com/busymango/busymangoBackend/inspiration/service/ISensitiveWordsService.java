package com.busymango.busymangoBackend.inspiration.service;

import java.time.LocalDateTime;
import java.util.Set;

public interface ISensitiveWordsService {
    Set<String> readSensitiveWords();
    LocalDateTime getLastCreatedAt();
}