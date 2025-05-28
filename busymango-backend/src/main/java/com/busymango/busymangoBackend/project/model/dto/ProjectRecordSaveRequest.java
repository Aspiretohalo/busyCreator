package com.busymango.busymangoBackend.project.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class ProjectRecordSaveRequest {
    /**
     * 项目记录ID，主键，自增
     */
    private Long id;

    /**
     * 项目ID，关联项目表
     */
    private Long projectId;

    /**
     * 项目内容，当前版本的快照
     */
    private String content;

    /**
     * 该记录是否已提交保存：0为个人暂存，1为已提交的版本
     */
    private String isSubmit;
}
