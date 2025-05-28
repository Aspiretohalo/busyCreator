package com.busymango.busymangoBackend.user.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 *
 * @author caoyanghalo@qq.com
 */

@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
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
    private static final long serialVersionUID = 1L;
}