package com.busymango.busymangoBackend.inspiration.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 灵感表
 * @TableName bcmcreate_inspiration
 */
@TableName(value ="bcmcreate_inspiration")
@Data
public class Inspiration {
    /**
     * ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 灵感主题
     */
    private String title;

    /**
     * 灵感内容
     */
    private String content;

    /**
     * 标签，多个标签用逗号分隔
     */
    private String tags;

    /**
     * 灵感类型：小说、广告、新闻
     */
    private String type;

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
     * 删除标记，逻辑删除
     */
    private String deleteFlag;
}