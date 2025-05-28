package com.busymango.busymangoBackend.AI.service;

import com.busymango.busymangoBackend.AI.model.dto.AIQuestionRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AIChatService {
    public SseEmitter getAnswer(AIQuestionRequest aiQuestionRequest);
}
