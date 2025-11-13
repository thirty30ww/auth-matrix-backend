package com.thirty.common.utils;

/**
 * 日志上下文，用于标记当前请求是否需要记录操作日志
 * 放在common模块，供所有模块使用
 */
public class LogContext {

    /**
    * 日志上下文数据
    */
    public record LogContextData(Object logOperation, Long startTime) { }

    private static final ThreadLocal<LogContextData> LOG_CONTEXT = new ThreadLocal<>();

    /**
     * 设置日志上下文（包含日志对象和开始时间）
     */
    public static void setLogContext(Object logOperation, Long startTime) {
        LOG_CONTEXT.set(new LogContextData(logOperation, startTime));
    }

    /**
     * 获取日志操作对象
     */
    public static Object getLogOperation() {
        LogContextData data = LOG_CONTEXT.get();
        return data != null ? data.logOperation() : null;
    }

    /**
     * 获取开始时间
     */
    public static Long getStartTime() {
        LogContextData data = LOG_CONTEXT.get();
        return data != null ? data.startTime() : null;
    }

    /**
     * 检查当前请求是否需要记录日志
     */
    public static boolean needLogOperation() {
        return LOG_CONTEXT.get() != null;
    }

    /**
     * 清理ThreadLocal，避免内存泄漏
     */
    public static void clear() {
        LOG_CONTEXT.remove();
    }
}