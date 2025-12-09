package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.enums.model.MethodType;
import com.thirty.common.enums.model.OperationType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户操作日志表
 * @TableName log_operation
 */
@TableName(value ="log_operation")
@Data
@Accessors(chain = true)
public class LogOperation implements Serializable {
    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 操作的模块
     */
    private String module;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作的类型(SELECT OR UPDATE...)
     */
    private OperationType type;

    /**
     * 操作的方法(POST OR GET...)
     */
    private MethodType method;

    /**
     * 返回的操作码
     */
    private Integer code;

    /**
     * 请求的URL
     */
    private String url;

    /**
     * 发出请求的IP地址
     */
    private String ip;

    /**
     * 请求的参数
     */
    private String requestParam;

    /**
     * 相应的参数
     */
    private String responseParam;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作用的时间(单位是毫秒)
     */
    private Integer operateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 获取日志操作的代码列表
     * @param logOperations 日志操作列表
     * @return 代码列表
     */
    public static List<Integer> getCodes(List<LogOperation> logOperations) {
        return logOperations.stream().map(LogOperation::getCode).distinct().collect(Collectors.toList());
    }

    /**
     * 获取日志操作的模块列表
     * @param logOperations 日志操作列表
     * @return 模块列表
     */
    public static List<String> getModules(List<LogOperation> logOperations) {
        return logOperations.stream().map(LogOperation::getModule).distinct().collect(Collectors.toList());
    }
}