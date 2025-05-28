package com.busymango.busymangoBackend.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 项目点赞收藏表
 * @TableName bcmcreate_project_thumb
 */
@TableName(value ="bcmcreate_project_thumb")
@Data
public class ProjectThumb {
    /**
     * ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 点赞thumb或收藏collect
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
     * 删除标记：0-未删除，1-已删除
     */
    private String deleteFlag;
}