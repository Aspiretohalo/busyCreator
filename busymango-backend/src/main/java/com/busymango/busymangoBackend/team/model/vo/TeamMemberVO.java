package com.busymango.busymangoBackend.team.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author caoyanghalo@qq.com
 */
@Data
public class TeamMemberVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 成员权限
     */
    private String teamRole;
    private static final long serialVersionUID = 1L;
}