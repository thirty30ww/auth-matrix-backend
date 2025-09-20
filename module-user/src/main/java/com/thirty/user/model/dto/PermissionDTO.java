package com.thirty.user.model.dto;

import com.thirty.user.enums.model.PermissionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionDTO {
    public interface Add {}
    public interface Modify {}

    @NotNull(groups = Modify.class, message = "视图ID不能为空")
    private Integer id;
    private String name;
    private String path;
    private String component;
    @NotNull(groups = {Add.class, Modify.class}, message = "视图类型不能为空")
    private PermissionType type;
    private String permissionCode;
    @NotNull(groups = {Add.class, Modify.class}, message = "父节点ID不能为空")
    private Integer parentNodeId;
    private Integer frontNodeId;
    private Integer behindNodeId;
    private String icon;
    private Boolean isValid;
}
