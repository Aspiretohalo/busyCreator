package com.busymango.busymangoBackend.project.model.dto;

import lombok.Data;

@Data
public class ProjectAuditUpdateDTO {
    /**
     * 项目审核记录ID
     */
    private Long projectAuditId;

    /**
     * 项目审核记录状态
     */
    private String status;
}
