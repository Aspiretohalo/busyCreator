package com.busymango.busymangoBackend.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @TableName user
 */
@Data
@TableName(value ="user")
public class User implements Serializable {
    /**
     * 用户ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户账号，使用手机号作为账号
     */
    private String userAccount;

    /**
     * 用户密码，使用MD5加密
     */
    private String userPassword;

    /**
     * 用户头像，存储在腾讯云COS
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String profile;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户角色：admin 或 user
     */
    private String role;

    /**
     * 逻辑删除标记：0-未删除，1-已删除
     */
    private String deleteFlag;

    /**
     * 记录创建时间
     */
    private Date createdAt;

    /**
     * 记录更新时间
     */
    private Date updatedAt;

    /**
     * 记录删除时间
     */
    private Date deletedAt;
    @TableField(exist = false)
    private String jwt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}