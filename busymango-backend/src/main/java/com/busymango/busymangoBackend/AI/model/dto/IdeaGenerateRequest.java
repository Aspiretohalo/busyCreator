package com.busymango.busymangoBackend.AI.model.dto;

import lombok.Data;

@Data
public class IdeaGenerateRequest {
    private String type;
    private String genre;
    private String theme;
    private String description;
    private String content;
}
