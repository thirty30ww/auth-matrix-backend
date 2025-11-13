package com.thirty.user.model.dto;

import com.thirty.common.enums.model.Status;
import com.thirty.common.model.dto.BaseListDTO;
import com.thirty.user.enums.model.LoginType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogLoginDTO extends BaseListDTO {
    private String browser;
    private String operatingSystem;
    private LoginType type;
    private Status status;
    private String name;
}
