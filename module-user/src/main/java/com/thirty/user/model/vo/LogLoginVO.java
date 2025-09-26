package com.thirty.user.model.vo;

import com.thirty.common.enums.model.Status;
import com.thirty.user.enums.model.LoginType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogLoginVO {
    private Long id;
    private String name;
    private String ip;
    private String browser;
    private String operatingSystem;
    private String deviceModel;
    private LoginType type;
    private Status status;
    private String errorMessage;
    private String location;
    private Integer operateTime;
    private LocalDateTime createTime;
}
