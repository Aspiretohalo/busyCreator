package com.busymango.busymangoBackend.project.model.dto;

import lombok.Data;

@Data
public class ProjectThumbCountDTO {
    private Long thumbCount; // 点赞数
    private Long bookmarkCount; // 收藏数
}
