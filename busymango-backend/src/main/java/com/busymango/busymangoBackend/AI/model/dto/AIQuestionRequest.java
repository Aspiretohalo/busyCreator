package com.busymango.busymangoBackend.AI.model.dto;

import lombok.Data;

@Data
public class AIQuestionRequest {
    private Long sessionId;
    private String question;
    private String context;
    private String type;
}
