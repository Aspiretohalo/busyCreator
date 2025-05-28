package com.busymango.busymangoBackend.core.model.dto.interfaceAccess;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口访问情况表
 *
 * @TableName bcmcreate_interface_access
 */
@Data
public class InterfaceAccessDTO implements Serializable {
    /**
     * 接口路径
     */
    private String interfacePath;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求携带参数
     */
    private String requestParams;

    /**
     * 响应时长（单位：毫秒）
     */
    private Long responseTime;

    /**
     * 用户ID
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}