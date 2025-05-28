package com.busymango.busymangoBackend.project.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProjectAuditDTO {

    /**
     * 项目ID，关联项目表
     */
    private Long projectId;
    /**
     * 项目记录ID
     */
    private Long projectRecordId;
}
