package com.busymango.busymangoBackend.core.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {
    /**
     * 计算从指定时间到当前时间的天数差
     *
     * @param createdAt 用户创建时间
     * @return 加入天数
     */
    public static long calculateDaysSinceJoin(Date createdAt) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime createdLocalDateTime = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 计算两个时间之间的天数差
        return ChronoUnit.DAYS.between(createdLocalDateTime, now);
    }

}