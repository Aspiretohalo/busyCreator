package com.busymango.busymangoBackend.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员信息表
 * @TableName user_vip
 */
@TableName(value ="user_vip")
public class UserVip implements Serializable {
    /**
     * 会员的唯一标识符
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 与用户表关联的用户ID
     */
    private Long userid;

    /**
     * 会员的等级或类别
     */
    private String membershiplevel;

    /**
     * 会员的当前状态，如活跃、暂停、过期等
     */
    private String membershipstatus;

    /**
     * 会员注册或加入会员计划的日期
     */
    private Date registrationdate;

    /**
     * 会员资格开始生效的日期
     */
    private Date effectivedate;

    /**
     * 会员资格到期的日期
     */
    private Date expirationdate;

    /**
     * 会员累积的积分数，默认为0
     */
    private Integer points;

    /**
     * 会员的联系电话
     */
    private String phonenumber;

    /**
     * 会员的电子邮件地址
     */
    private String email;

    /**
     * 会员的邮寄地址
     */
    private String address;

    /**
     * 会员的生日
     */
    private Date birthday;

    /**
     * 会员信息创建的时间，默认为当前时间
     */
    private Date creationdate;

    /**
     * 会员信息最后一次更新的时间，每次更新记录时自动更新
     */
    private Date lastupdateddate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 会员的唯一标识符
     */
    public Long getId() {
        return id;
    }

    /**
     * 会员的唯一标识符
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 与用户表关联的用户ID
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 与用户表关联的用户ID
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 会员的等级或类别
     */
    public String getMembershiplevel() {
        return membershiplevel;
    }

    /**
     * 会员的等级或类别
     */
    public void setMembershiplevel(String membershiplevel) {
        this.membershiplevel = membershiplevel;
    }

    /**
     * 会员的当前状态，如活跃、暂停、过期等
     */
    public String getMembershipstatus() {
        return membershipstatus;
    }

    /**
     * 会员的当前状态，如活跃、暂停、过期等
     */
    public void setMembershipstatus(String membershipstatus) {
        this.membershipstatus = membershipstatus;
    }

    /**
     * 会员注册或加入会员计划的日期
     */
    public Date getRegistrationdate() {
        return registrationdate;
    }

    /**
     * 会员注册或加入会员计划的日期
     */
    public void setRegistrationdate(Date registrationdate) {
        this.registrationdate = registrationdate;
    }

    /**
     * 会员资格开始生效的日期
     */
    public Date getEffectivedate() {
        return effectivedate;
    }

    /**
     * 会员资格开始生效的日期
     */
    public void setEffectivedate(Date effectivedate) {
        this.effectivedate = effectivedate;
    }

    /**
     * 会员资格到期的日期
     */
    public Date getExpirationdate() {
        return expirationdate;
    }

    /**
     * 会员资格到期的日期
     */
    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    /**
     * 会员累积的积分数，默认为0
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * 会员累积的积分数，默认为0
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * 会员的联系电话
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * 会员的联系电话
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * 会员的电子邮件地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 会员的电子邮件地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 会员的邮寄地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 会员的邮寄地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 会员的生日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 会员的生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 会员信息创建的时间，默认为当前时间
     */
    public Date getCreationdate() {
        return creationdate;
    }

    /**
     * 会员信息创建的时间，默认为当前时间
     */
    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    /**
     * 会员信息最后一次更新的时间，每次更新记录时自动更新
     */
    public Date getLastupdateddate() {
        return lastupdateddate;
    }

    /**
     * 会员信息最后一次更新的时间，每次更新记录时自动更新
     */
    public void setLastupdateddate(Date lastupdateddate) {
        this.lastupdateddate = lastupdateddate;
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
        UserVip other = (UserVip) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getMembershiplevel() == null ? other.getMembershiplevel() == null : this.getMembershiplevel().equals(other.getMembershiplevel()))
            && (this.getMembershipstatus() == null ? other.getMembershipstatus() == null : this.getMembershipstatus().equals(other.getMembershipstatus()))
            && (this.getRegistrationdate() == null ? other.getRegistrationdate() == null : this.getRegistrationdate().equals(other.getRegistrationdate()))
            && (this.getEffectivedate() == null ? other.getEffectivedate() == null : this.getEffectivedate().equals(other.getEffectivedate()))
            && (this.getExpirationdate() == null ? other.getExpirationdate() == null : this.getExpirationdate().equals(other.getExpirationdate()))
            && (this.getPoints() == null ? other.getPoints() == null : this.getPoints().equals(other.getPoints()))
            && (this.getPhonenumber() == null ? other.getPhonenumber() == null : this.getPhonenumber().equals(other.getPhonenumber()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getCreationdate() == null ? other.getCreationdate() == null : this.getCreationdate().equals(other.getCreationdate()))
            && (this.getLastupdateddate() == null ? other.getLastupdateddate() == null : this.getLastupdateddate().equals(other.getLastupdateddate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getMembershiplevel() == null) ? 0 : getMembershiplevel().hashCode());
        result = prime * result + ((getMembershipstatus() == null) ? 0 : getMembershipstatus().hashCode());
        result = prime * result + ((getRegistrationdate() == null) ? 0 : getRegistrationdate().hashCode());
        result = prime * result + ((getEffectivedate() == null) ? 0 : getEffectivedate().hashCode());
        result = prime * result + ((getExpirationdate() == null) ? 0 : getExpirationdate().hashCode());
        result = prime * result + ((getPoints() == null) ? 0 : getPoints().hashCode());
        result = prime * result + ((getPhonenumber() == null) ? 0 : getPhonenumber().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getCreationdate() == null) ? 0 : getCreationdate().hashCode());
        result = prime * result + ((getLastupdateddate() == null) ? 0 : getLastupdateddate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", membershiplevel=").append(membershiplevel);
        sb.append(", membershipstatus=").append(membershipstatus);
        sb.append(", registrationdate=").append(registrationdate);
        sb.append(", effectivedate=").append(effectivedate);
        sb.append(", expirationdate=").append(expirationdate);
        sb.append(", points=").append(points);
        sb.append(", phonenumber=").append(phonenumber);
        sb.append(", email=").append(email);
        sb.append(", address=").append(address);
        sb.append(", birthday=").append(birthday);
        sb.append(", creationdate=").append(creationdate);
        sb.append(", lastupdateddate=").append(lastupdateddate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}