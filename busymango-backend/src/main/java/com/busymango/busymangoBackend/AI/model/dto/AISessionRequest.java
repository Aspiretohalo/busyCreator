package com.busymango.busymangoBackend.AI.model.dto;

import lombok.Data;

@Data
public class AISessionRequest {
    private Long userId;
    private String sessionTitle;

}
