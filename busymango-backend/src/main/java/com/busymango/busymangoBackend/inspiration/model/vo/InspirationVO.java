package com.busymango.busymangoBackend.inspiration.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 灵感表
 * @TableName bcmcreate_inspiration
 */
@TableName(value ="bcmcreate_inspiration")
@Data
public class InspirationVO {
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

}