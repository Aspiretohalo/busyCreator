package com.busymango.busymangoBackend.task.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * 任务表
 *
 * @TableName bcmcreate_task
 */
@TableName(value = "bcmcreate_task")
@Data
public class Task {
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
     * 所属项目名称
     */
    private String projectName;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 任务对象id
     */
    private Long assigneeId;

    /**
     * 任务对象名
     */
    private String assigneeName;

    /**
     * 完成日期
     */
    private Date finishDate;

    /**
     * 耗费工时
     */
    private Integer workTime;

    /**
     * 备注
     */
    private String note;

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
    private String isSubmitted;
}