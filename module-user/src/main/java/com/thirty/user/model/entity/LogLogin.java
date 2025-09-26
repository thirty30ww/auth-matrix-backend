package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.thirty.common.enums.model.Status;
import com.thirty.user.enums.model.LoginType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录日志表
 * @TableName log_login
 */
@TableName(value ="log_login")
@Data
@Accessors(chain = true)
public class LogLogin implements Serializable {
    /**
     * 登录日志ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作人ID
     */
    private Integer userId;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 操作IP地址
     */
    private String ip;

    /**
     * 操作系统
     */
    private String operatingSystem;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 登录类型(LOGIN/LOGOUT/REFRESH)
     */
    private LoginType type;

    /**
     * 登录状态(SUCCESS/FAILED)
     */
    private Status status;

    /**
     * 失败原因
     */
    private String errorMessage;

    /**
     * 操作时间
     */
    private Integer operateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 获取浏览器列表
     * @param logLogins 登录日志列表
     * @return 浏览器列表
     */
    public static List<String> getBrowsers(List<LogLogin> logLogins) {
        return logLogins.stream().map(LogLogin::getBrowser).collect(Collectors.toList());
    }

    /**
     * 获取操作系统列表
     * @param logLogins 登录日志列表
     * @return 操作系统列表
     */
    public static List<String> getOperatingSystems(List<LogLogin> logLogins) {
        return logLogins.stream().map(LogLogin::getOperatingSystem).collect(Collectors.toList());
    }
}