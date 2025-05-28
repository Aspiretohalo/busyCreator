package com.busymango.busymangoBackend.core.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户接口访问情况表
 * @TableName bcmcreate_interface_access
 */
@TableName(value ="bcmcreate_interface_access")
@Data
public class InterfaceAccess implements Serializable {
    /**
     * 访问情况ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 创建人员
     */
    private Long insertUser;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新人员
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除人员
     */
    private Long deleteUser;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 逻辑删除标记：0-未删除，1-已删除
     */
    private String deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}