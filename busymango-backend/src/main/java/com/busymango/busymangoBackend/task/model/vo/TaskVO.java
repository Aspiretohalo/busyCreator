package com.busymango.busymangoBackend.task.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 任务表
 *
 * @TableName bcmcreate_task
 */
@Data
public class TaskVO {
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
     * 0表示下发出去，1表示收到
     */
    private String status;
    /**
     * 0表示未提交，1表示已提交
     */
    private String isSubmitted;
}