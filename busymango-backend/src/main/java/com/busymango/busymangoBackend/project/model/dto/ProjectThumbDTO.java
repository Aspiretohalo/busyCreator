package com.busymango.busymangoBackend.project.model.dto;

import lombok.Data;

@Data
public class ProjectThumbDTO {
    /**
     * 项目记录ID
     */
    private Long projectId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 点赞或收藏
     * thumb|collect
     */
    private String type;

}
