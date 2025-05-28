package com.busymango.busymangoBackend.project.model.dto;

import lombok.Data;

@Data
public class ProjectRecordSubmitRequest {
    /**
     * 项目记录ID，主键，自增
     */
    private Long id;

    /**
     * 提交备注
     */
    private String recordComment;

}
