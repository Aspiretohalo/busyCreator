package com.busymango.busymangoBackend.user.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author caoyanghalo@qq.com
 */
@Data
public class UserVO implements Serializable {

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
     * 记录创建时间
     */
    private Date createdAt;

    private static final long serialVersionUID = 1L;
}