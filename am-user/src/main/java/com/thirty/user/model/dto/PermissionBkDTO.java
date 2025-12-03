package com.thirty.user.model.dto;

import com.thirty.user.enums.model.PermissionBkType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionBkDTO {
    public interface Add {}
    public interface Modify {}

    @NotNull(groups = Modify.class, message = "权限ID不能为空")
    private Integer id;
    @NotBlank(groups = {Add.class, Modify.class}, message = "权限名称不能为空")
    private String name;
    private String path;
    private String component;
    @NotNull(groups = {Add.class, Modify.class}, message = "权限类型不能为空")
    private PermissionBkType type;
    private String permissionCode;
    @NotNull(groups = {Add.class, Modify.class}, message = "父节点ID不能为空")
    private Integer parentNodeId;
    private Integer frontNodeId;
    private Integer behindNodeId;
    private String icon;
    private Boolean isValid;
}
