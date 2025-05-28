package com.busymango.busymangoBackend.project.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.busymango.busymangoBackend.user.model.vo.UserVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目内容及更新记录表
 * @TableName bcmcreate_project_record
 */
@Data
public class ProjectRecordVO implements Serializable {
    /**
     * 项目记录ID，主键，自增
     */
    private Long id;

    /**
     * 项目ID，关联项目表
     */
    private Long projectId;

    /**
     * 项目内容，当前版本的快照
     */
    private String content;

    /**
     * 新增内容
     */
    private String contentAdd;

    /**
     * 减少的内容
     */
    private String contentMinus;

    /**
     * 团队ID，关联团队表
     */
    private Long teamId;

    /**
     * 创建该记录的用户ID
     */
    private Long insertUser;
    /**
     * 创建该记录的用户ID
     */
    private UserVO insertUserInfo;
    /**
     * 记录创建时间
     */
    private Date insertTime;
    /**
     * 该记录是否已提交保存：0为个人暂存，1为已提交的版本
     */
    private String isSubmit;
    /**
     * 备注评论	每次提交都要有备注评论，到底本次修改了什么
     */
    private String recordComment;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}