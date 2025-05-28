package com.busymango.busymangoBackend.project.model.dto;

import lombok.Data;

@Data
public class ProjectThumbRequest {
    /**
     * 项目记录ID
     */
    private Long projectId;

    /**
     * 点赞或收藏
     * thumb|collect
     */
    private String type;

}
