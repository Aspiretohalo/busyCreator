package com.busymango.busymangoBackend.team.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 团队信息表
 * @TableName bcmcreate_team
 */
@TableName(value ="bcmcreate_team")
@Data
public class Team implements Serializable {
    /**
     * 团队ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 团队名
     */
    private String name;
    /**
     * 团队头像
     */
    private String avatar;

    /**
     * 团队简介
     */
    private String profile;
    /**
     * 团队管理员ID
     */
    private Long adminId;

    /**
     * 团队成员列表，以逗号分隔的用户ID
     */
    private String memberList;

    /**
     * 创建该团队的用户ID
     */
    private Long insertUser;

    /**
     * 记录创建时间
     */
    private Date insertTime;

    /**
     * 最后更新该团队的用户ID
     */
    private Long updateUser;

    /**
     * 记录最后更新时间
     */
    private Date updateTime;

    /**
     * 删除该团队的用户ID
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