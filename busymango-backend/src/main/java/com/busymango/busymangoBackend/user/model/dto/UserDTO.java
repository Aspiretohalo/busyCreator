package com.busymango.busymangoBackend.user.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private Long userId;
    private String role;

}
