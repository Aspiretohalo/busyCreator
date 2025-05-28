package com.busymango.busymangoBackend.chat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName chat_groupmember
 */
@TableName(value ="chat_groupmember")
public class ChatGroupmember implements Serializable {
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 群聊ID
     */
    private Long groupId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 成员角色（群主、管理员、普通成员）
     */
    private String role;

    /**
     * 加入时间
     */
    private Date joinedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 记录ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 群聊ID
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 群聊ID
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 成员角色（群主、管理员、普通成员）
     */
    public String getRole() {
        return role;
    }

    /**
     * 成员角色（群主、管理员、普通成员）
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 加入时间
     */
    public Date getJoinedAt() {
        return joinedAt;
    }

    /**
     * 加入时间
     */
    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChatGroupmember other = (ChatGroupmember) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getJoinedAt() == null ? other.getJoinedAt() == null : this.getJoinedAt().equals(other.getJoinedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getJoinedAt() == null) ? 0 : getJoinedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", userId=").append(userId);
        sb.append(", role=").append(role);
        sb.append(", joinedAt=").append(joinedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}