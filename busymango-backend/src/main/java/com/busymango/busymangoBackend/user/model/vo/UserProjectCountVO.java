package com.busymango.busymangoBackend.user.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户项目数
 *
 * @author caoyanghalo@qq.com
 */
@Data
public class UserProjectCountVO implements Serializable {

    /**
     * 项目数
     */
    private Long projectCount;

    /**
     * 作品数
     */
    private Long worksCount;

    private static final long serialVersionUID = 1L;
}