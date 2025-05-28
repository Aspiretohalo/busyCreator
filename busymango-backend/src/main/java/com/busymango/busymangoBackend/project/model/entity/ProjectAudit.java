package com.busymango.busymangoBackend.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 项目审核表
 * @TableName bcmcreate_project_audit
 */
@TableName(value ="bcmcreate_project_audit")
@Data
public class ProjectAudit implements Serializable {
    /**
     * ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 所属项目记录id
     */
    private Long projectRecordId;

    /**
     * 审核进度状态，审核中11，审核通过21，审核退回22
     */
    private String status;

    /**
     * 创建人员
     */
    private Long insertUser;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新人员
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除人员
     */
    private Long deleteUser;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 删除标记，逻辑删除
     */
    private String deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}