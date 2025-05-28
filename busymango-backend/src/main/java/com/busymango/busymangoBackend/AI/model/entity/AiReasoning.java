package com.busymango.busymangoBackend.AI.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * AI回复思维链表
 * @TableName bcmcreate_ai_reasoning
 */
@TableName(value ="bcmcreate_ai_reasoning")
@Data
public class AiReasoning implements Serializable {
    /**
     * AI回复思维链ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属问题ID
     */
    private Long questionId;

    /**
     * 思维链内容
     */
    private String reasoningContent;

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