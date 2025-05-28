package com.busymango.busymangoBackend.project.model.enums;

/**
 * 审核状态枚举
 *
 * @author caoyanghalo@qq.com
 */
public enum ProjectAuditStatusEnum {
    /**
     * 审核中
     */
    AUDITING("11"),
    /**
     * 审核通过
     */
    APPROVED("21"),
    /**
     * 审核退回
     */
    REJECTED("22");

    private final String status;

    ProjectAuditStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * 根据状态字符串获取枚举
     *
     * @param status 状态字符串
     * @return 对应的枚举
     */
    public static ProjectAuditStatusEnum fromStatus(String status) {
        for (ProjectAuditStatusEnum auditStatus : ProjectAuditStatusEnum.values()) {
            if (auditStatus.getStatus().equals(status)) {
                return auditStatus;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }
}
