package com.busymango.busymangoBackend.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {
    /**
     * 计算逗号分隔字符串中的成员数量
     *
     * @param memberList 逗号分隔的字符串
     * @return 成员数量
     */
    public static int countMembers(String memberList) {
        if (memberList == null || memberList.isEmpty()) {
            return 0;
        }
        // 使用 split 方法按逗号分割字符串，并计算长度
        return memberList.split(",", -1).length;
    }

    /**
     * 将逗号分隔的字符串转换为 List<Long>
     *
     * @param memberList 逗号分隔的字符串
     * @return 转换后的列表
     */
    public static List<Long> stringToLongList(String memberList) {
        if (memberList == null || memberList.isEmpty()) {
            return new ArrayList<>(); // 返回一个空列表
        }
        // 使用 Stream API 将字符串分割并转换为 Long 类型
        return Arrays.stream(memberList.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 将逗号分隔的字符串转换为 List<String>
     *
     * @param str 逗号分隔的字符串
     * @return 转换后的列表
     */
    public static List<String> stringToList(String str) {
        if (str == null || str.isEmpty()) {
            return Arrays.asList(); // 返回一个空列表
        }
        return Arrays.asList(str.split(","));
    }

    /**
     * 将 List<String> 转换为逗号分隔的字符串
     *
     * @param list 字符串列表
     * @return 转换后的逗号分隔字符串
     */
    public static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return ""; // 返回空字符串
        }
        return String.join(",", list);
    }

    /**
     * 将字符串中的双引号替换为单引号
     * @param input 原始字符串
     * @return 替换后的字符串
     */
    public static String replaceDoubleQuotesWithSingleQuotes(String input) {
        if (input == null) {
            return null; // 如果输入为null，直接返回null
        }
        return input.replace("\"", "'"); // 替换双引号为单引号
    }
}