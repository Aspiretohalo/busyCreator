package com.busymango.busymangoBackend.team.model.enums;

public enum TeamRoleEnums {
    /**
     * 管理员角色
     */
    ADMIN("ADMIN", "管理员"),
    /**
     * 普通成员角色
     */
    MEMBER("MEMBER", "普通成员");

    private final String code;
    private final String description;

    TeamRoleEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据 code 获取对应的 TeamRoleEnums
     *
     * @param code 权限代码
     * @return 对应的 TeamRoleEnums，如果不存在则返回 null
     */
    public static TeamRoleEnums fromCode(String code) {
        for (TeamRoleEnums role : TeamRoleEnums.values()) {
            if (role.getCode().equalsIgnoreCase(code)) {
                return role;
            }
        }
        return null;
    }
}