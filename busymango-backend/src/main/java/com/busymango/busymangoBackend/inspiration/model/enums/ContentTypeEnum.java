package com.busymango.busymangoBackend.inspiration.model.enums;

/**
 * 审核状态枚举
 *
 * @author caoyanghalo@qq.com
 */
public enum ContentTypeEnum {
    /**
     * 小说
     */
    NOVEL("novel", "小说"),
    /**
     * 广告
     */
    AD("ad", "广告"),
    /**
     * 新闻
     */
    NEWS("news", "新闻"),
    /**
     * 大纲
     */
    OUTLINE("outline", "大纲"),
    /**
     * 评论
     */
    COMMENT("comment", "评论");

    private final String type;
    private final String description;

    ContentTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据类型字符串获取枚举
     *
     * @param type 类型字符串（英文）
     * @return 对应的枚举
     */
    public static ContentTypeEnum fromType(String type) {
        for (ContentTypeEnum contentType : ContentTypeEnum.values()) {
            if (contentType.getType().equals(type)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }

    /**
     * 根据中文描述获取枚举
     *
     * @param description 中文描述
     * @return 对应的枚举
     */
    public static ContentTypeEnum fromDescription(String description) {
        for (ContentTypeEnum contentType : ContentTypeEnum.values()) {
            if (contentType.getDescription().equals(description)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException("Invalid description: " + description);
    }
}