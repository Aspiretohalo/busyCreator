package com.busymango.busymangoBackend.team.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 团队信息表
 *
 * @TableName bcmcreate_team
 */
@TableName(value = "bcmcreate_team")
@Data
public class TeamVO implements Serializable {
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
    private Integer memberCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}