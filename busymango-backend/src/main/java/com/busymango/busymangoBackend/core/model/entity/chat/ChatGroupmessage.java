package com.busymango.busymangoBackend.chat.entity.chat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName chat_groupmessage
 */
@TableName(value ="chat_groupmessage")
public class ChatGroupmessage implements Serializable {
    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 群聊ID
     */
    private Long groupId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date timestamp;

    /**
     * 消息状态（已发送、已接收、已读等）
     */
    private String status;

    /**
     * 消息类型（文本、图片、文件等）
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 消息ID
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
     * 发送者ID
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * 发送者ID
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 发送时间
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * 发送时间
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 消息状态（已发送、已接收、已读等）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 消息状态（已发送、已接收、已读等）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 消息类型（文本、图片、文件等）
     */
    public String getType() {
        return type;
    }

    /**
     * 消息类型（文本、图片、文件等）
     */
    public void setType(String type) {
        this.type = type;
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
        ChatGroupmessage other = (ChatGroupmessage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
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
        sb.append(", senderId=").append(senderId);
        sb.append(", content=").append(content);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", status=").append(status);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}