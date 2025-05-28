package com.busymango.busymangoBackend.project.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProjectVO implements Serializable {
    /**
     * 项目ID，主键，自增
     */
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
    private List tagList;
    /**
     * 标签
     */
    private String tags;
    /**
     * 团队ID，每个项目对应一个团队
     */
    private Long teamId;
    /**
     * 项目当前版本，对应项目内容表的ID
     */
    private Long version;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // 新增字段
    private Long thumbCount; // 点赞数
    private Long bookmarkCount; // 收藏数
    private TeamVO teamInfo; // 团队信息
    private String content;
}