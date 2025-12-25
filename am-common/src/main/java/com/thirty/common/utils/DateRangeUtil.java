package com.thirty.common.utils;

import com.thirty.common.enums.model.DateRangeType;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 日期范围工具类，用于处理不同时间范围的查询
 */
public class DateRangeUtil {


    public record TimeRangeInfo(LocalDateTime startTime, LocalDateTime endTime,
                                Function<LocalDateTime, Integer> groupingFunction) {}

    /**
     * 根据日期过滤类型获取时间范围信息和分组函数
     * @param type 日期过滤类型
     * @param now 当前时间
     * @return 时间范围信息
     */
    public static TimeRangeInfo getTimeRangeInfo(DateRangeType type, LocalDateTime now) {
        return switch (type) {
            case TODAY -> getTodayTimeRange(now);
            case THIS_WEEK -> getWeekTimeRange(now);
            case THIS_MONTH -> getMonthTimeRange(now);
            case THIS_YEAR -> getYearTimeRange(now);
        };
    }

    /**
     * 获取指定时间范围内的所有时间单位
     * @param type 日期过滤类型
     * @param now 当前时间
     * @return 所有可能的时间单位列表
     */
    public static List<Integer> getAllTimeUnits(DateRangeType type, LocalDateTime now) {
        return switch (type) {
            case TODAY -> getTodayTimeUnits();
            case THIS_WEEK -> getWeekTimeUnits();
            case THIS_MONTH -> getMonthTimeUnits(now);
            case THIS_YEAR -> getYearTimeUnits();
        };
    }

    /**
     * 获取今天的开始时间，即0时0分0秒0毫秒
     * @param now 当前时间
     * @return 今天的开始时间
     */
    public static LocalDateTime getTodayStart(LocalDateTime now) {
        return now.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取本周的开始时间，即本周星期一0时0分0秒0毫秒
     * @param now 当前时间
     * @return 本周的开始时间
     */
    public static LocalDateTime getWeekStart(LocalDateTime now) {
        return now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取本月的开始时间，即本月1号0时0分0秒0毫秒
     * @param now 当前时间
     * @return 本月的开始时间
     */
    public static LocalDateTime getMonthStart(LocalDateTime now) {
        return now.withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取本年的开始时间，即本年1月1号0时0分0秒0毫秒
     * @param now 当前时间
     * @return 本年的开始时间
     */
    public static LocalDateTime getYearStart(LocalDateTime now) {
        return now.withMonth(1).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取今天的时间范围信息，从今天0时0分0秒0毫秒开始到当前时间
     * @param now 当前时间
     * @return 时间范围信息
     */
    private static TimeRangeInfo getTodayTimeRange(LocalDateTime now) {
        LocalDateTime todayStart = getTodayStart(now);
        return new TimeRangeInfo(
                todayStart,
                now,
                LocalDateTime::getHour  // 按小时分组
        );
    }

    /**
     * 获取本周的时间范围信息，从本周星期一0时0分0秒0毫秒开始到当前时间
     * @param now 当前时间
     * @return 时间范围信息
     */
    private static TimeRangeInfo getWeekTimeRange(LocalDateTime now) {
        LocalDateTime weekStart = getWeekStart(now);
        return new TimeRangeInfo(
                weekStart,
                now,
                dateTime -> dateTime.getDayOfWeek().getValue()  // 按星期几分组
        );
    }

    /**
     * 获取本月的时间范围信息，从本月1号0时0分0秒0毫秒开始到当前时间
     * @param now 当前时间
     * @return 时间范围信息
     */
    private static TimeRangeInfo getMonthTimeRange(LocalDateTime now) {
        LocalDateTime monthStart = getMonthStart(now);
        return new TimeRangeInfo(
                monthStart,
                now,
                LocalDateTime::getDayOfMonth  // 按日期分组
        );
    }

    /**
     * 获取本年的时间范围信息，从本年1月1号0时0分0秒0毫秒开始到当前时间
     * @param now 当前时间
     * @return 时间范围信息
     */
    private static TimeRangeInfo getYearTimeRange(LocalDateTime now) {
        LocalDateTime yearStart = getYearStart(now);
        return new TimeRangeInfo(
                yearStart,
                now,
                LocalDateTime::getMonthValue  // 按月份分组
        );
    }

    /**
     * 获取今天所有的小时单位
     * @return 今天所有的小时单位列表
     */
    private static List<Integer> getTodayTimeUnits() {
        List<Integer> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        return hours;
    }

    /**
     * 获取本周所有的星期单位
     * @return 本周所有的星期单位列表
     */
    private static List<Integer> getWeekTimeUnits() {
        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            days.add(i);
        }
        return days;
    }

    /**
     * 获取本月所有的日期单位
     * @param now 当前时间
     * @return 本月所有的日期单位列表
     */
    private static List<Integer> getMonthTimeUnits(LocalDateTime now) {
        List<Integer> days = new ArrayList<>();
        // 获取本月的天数
        int daysInMonth = now.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }
        return days;
    }

    /**
     * 获取本年所有的月份单位
     * @return 本年所有的月份单位列表
     */
    private static List<Integer> getYearTimeUnits() {
        List<Integer> months = new ArrayList<>();
        // 本年总是1-12月
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        return months;
    }
}