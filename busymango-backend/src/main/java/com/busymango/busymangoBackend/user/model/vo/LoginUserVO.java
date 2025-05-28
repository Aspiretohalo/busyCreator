package com.busymango.busymangoBackend.user.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 已登录用户视图（脱敏）
 **/
@Data
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
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
     * 用户角色：user/admin/ban
     */
    private String role;

    /**
     * 会员编号 membershipId
     */
    private Long vipId;

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

    /**
     * 登录凭证
     */
    private String jwt;
    /**
     * 用户项目数信息
     */
    private UserProjectCountVO userProjectCountVO;
    /**
     * 用户加入天数
     */
    private Long joinDays;
    private static final long serialVersionUID = 1L;
}