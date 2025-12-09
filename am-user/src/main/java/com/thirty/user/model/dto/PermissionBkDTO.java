package com.thirty.user.model.dto;

import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.dto.base.BasePermissionDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionBkDTO extends BasePermissionDTO {
    @NotNull(groups = {Add.class, Modify.class}, message = "权限类型不能为空")
    private PermissionBkType type;

    private String icon;
}
