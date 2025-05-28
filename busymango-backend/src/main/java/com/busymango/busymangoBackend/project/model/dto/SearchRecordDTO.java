package com.busymango.busymangoBackend.project.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索记录表
 * @TableName bcmcreate_search_record
 */
@Data
public class SearchRecordDTO implements Serializable {
    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 搜索用户ID
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}