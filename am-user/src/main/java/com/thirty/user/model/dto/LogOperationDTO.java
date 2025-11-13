package com.thirty.user.model.dto;

import com.thirty.common.enums.model.MethodType;
import com.thirty.common.enums.model.OperationType;
import com.thirty.common.model.dto.BaseListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogOperationDTO extends BaseListDTO {
    private String name;
    private String module;
    private String description;
    private Integer code;
    private OperationType type;
    private MethodType method;
}
