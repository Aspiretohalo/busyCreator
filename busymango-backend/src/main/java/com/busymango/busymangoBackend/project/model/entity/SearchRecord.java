package com.busymango.busymangoBackend.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 搜索记录表
 * @TableName bcmcreate_search_record
 */
@TableName(value ="bcmcreate_search_record")
@Data
public class SearchRecord implements Serializable {
    /**
     * 搜索记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 搜索用户ID
     */
    private Long userId;

    /**
     * 创建该记录的用户ID
     */
    private Long insertUser;

    /**
     * 记录创建时间
     */
    private Date insertTime;

    /**
     * 最后更新该记录的用户ID
     */
    private Long updateUser;

    /**
     * 记录最后更新时间
     */
    private Date updateTime;

    /**
     * 删除该记录的用户ID
     */
    private Long deleteUser;

    /**
     * 记录删除时间
     */
    private Date deleteTime;

    /**
     * 逻辑删除标记：0-未删除，1-已删除
     */
    private String deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}