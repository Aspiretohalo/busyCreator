package com.busymango.busymangoBackend.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目（作品）信息表
 *
 * @TableName bcmcreate_project
 */
@TableName(value = "bcmcreate_project")
@Data
public class Project implements Serializable {
    /**
     * 项目ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目简介
     */
    private String profile;
    /**
     * 封面
     */
    private String cover;
    /**
     * 标签
     */
    private String tags;
    /**
     * 项目当前版本，对应项目内容表的ID
     */
    private Long version;

    /**
     * 团队ID，每个项目对应一个团队
     */
    private Long teamId;

    /**
     * 是否公开为作品：0-否，1-是
     */
    private String isPublic;

    /**
     * 创建该项目的用户ID
     */
    private Long insertUser;

    /**
     * 记录创建时间
     */
    private Date insertTime;

    /**
     * 最后更新该项目的用户ID
     */
    private Long updateUser;

    /**
     * 记录最后更新时间
     */
    private Date updateTime;

    /**
     * 删除该项目的用户ID
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


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}