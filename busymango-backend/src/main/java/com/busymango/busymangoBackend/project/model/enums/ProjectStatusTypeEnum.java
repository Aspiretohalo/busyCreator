package com.busymango.busymangoBackend.project.model.enums;

/**
 * 点赞、收藏枚举
 *
 * @author caoyanghalo@qq.com
 */
public enum ProjectStatusTypeEnum {
    /**
     * 点赞
     */
    THUMB("thumb"),
    /**
     * 收藏
     */
    COLLECT("collect");

    private final String type;

    ProjectStatusTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据类型字符串获取枚举
     *
     * @param type 类型字符串
     * @return 对应的枚举
     */
    public static ProjectStatusTypeEnum fromType(String type) {
        for (ProjectStatusTypeEnum status : ProjectStatusTypeEnum.values()) {
            if (status.getType().equals(type)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }
}
