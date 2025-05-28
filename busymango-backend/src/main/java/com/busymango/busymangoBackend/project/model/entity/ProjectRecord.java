package com.busymango.busymangoBackend.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 项目内容及更新记录表
 *
 * @TableName bcmcreate_project_record
 */
@TableName(value = "bcmcreate_project_record")
@Data
public class ProjectRecord implements Serializable {
    /**
     * 项目记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
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
     * 新增内容
     */
    private String contentAdd;

    /**
     * 减少的内容
     */
    private String contentMinus;

    /**
     * 团队ID，关联团队表
     */
    private Long teamId;

    /**
     * 创建该记录的用户ID
     */
    private Long insertUser;

    /**
     * 记录创建时间
     */
    private Date insertTime;

    /**
     * 最后更新该记录的用户ID
     */
    private Long updateUser;

    /**
     * 记录最后更新时间
     */
    private Date updateTime;

    /**
     * 删除该记录的用户ID
     */
    private Long deleteUser;

    /**
     * 记录删除时间
     */
    private Date deleteTime;

    /**
     * 逻辑删除标记：0-未删除，1-已删除
     */
    private String deleteFlag;
    /**
     * 该记录是否已提交保存：0为个人暂存，1为已提交的版本
     */
    private String isSubmit;
    /**
     * 备注评论	每次提交都要有备注评论，到底本次修改了什么
     */
    private String recordComment;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}