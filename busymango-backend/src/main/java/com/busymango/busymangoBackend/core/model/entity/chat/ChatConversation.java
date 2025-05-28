package com.busymango.busymangoBackend.chat.entity.chat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName chat_conversation
 */
@TableName(value ="chat_conversation")
public class ChatConversation implements Serializable {
    /**
     * 会话ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户1 ID
     */
    private Long user1Id;

    /**
     * 用户2 ID
     */
    private Long user2Id;

    /**
     * 最后一条消息ID
     */
    private Long lastMessageId;

    /**
     * 最后一条消息时间
     */
    private Date lastMessageTime;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 会话ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户1 ID
     */
    public Long getUser1Id() {
        return user1Id;
    }

    /**
     * 用户1 ID
     */
    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    /**
     * 用户2 ID
     */
    public Long getUser2Id() {
        return user2Id;
    }

    /**
     * 用户2 ID
     */
    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }

    /**
     * 最后一条消息ID
     */
    public Long getLastMessageId() {
        return lastMessageId;
    }

    /**
     * 最后一条消息ID
     */
    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /**
     * 最后一条消息时间
     */
    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * 最后一条消息时间
     */
    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * 创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 更新时间
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 更新时间
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
        ChatConversation other = (ChatConversation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUser1Id() == null ? other.getUser1Id() == null : this.getUser1Id().equals(other.getUser1Id()))
            && (this.getUser2Id() == null ? other.getUser2Id() == null : this.getUser2Id().equals(other.getUser2Id()))
            && (this.getLastMessageId() == null ? other.getLastMessageId() == null : this.getLastMessageId().equals(other.getLastMessageId()))
            && (this.getLastMessageTime() == null ? other.getLastMessageTime() == null : this.getLastMessageTime().equals(other.getLastMessageTime()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUser1Id() == null) ? 0 : getUser1Id().hashCode());
        result = prime * result + ((getUser2Id() == null) ? 0 : getUser2Id().hashCode());
        result = prime * result + ((getLastMessageId() == null) ? 0 : getLastMessageId().hashCode());
        result = prime * result + ((getLastMessageTime() == null) ? 0 : getLastMessageTime().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user1Id=").append(user1Id);
        sb.append(", user2Id=").append(user2Id);
        sb.append(", lastMessageId=").append(lastMessageId);
        sb.append(", lastMessageTime=").append(lastMessageTime);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}