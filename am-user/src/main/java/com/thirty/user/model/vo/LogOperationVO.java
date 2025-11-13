package com.thirty.user.model.vo;

import com.thirty.common.enums.model.MethodType;
import com.thirty.common.enums.model.OperationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogOperationVO {
    private Integer id;
    private Integer userId;
    private String name;
    private String module;
    private String description;
    private OperationType type;
    private MethodType method;
    private Integer code;
    private String url;
    private String ip;
    private String requestParam;
    private String responseParam;
    private String errorMessage;
    private Integer operateTime;
    private LocalDateTime createTime;
}
