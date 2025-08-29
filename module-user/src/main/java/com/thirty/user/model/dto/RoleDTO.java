package com.thirty.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleDTO {
    
    // 验证组接口
    public interface Add {}
    public interface Update {}
    
    @NotNull(groups = Update.class, message = "角色ID不能为空")
    private Integer id;
    
    @NotBlank(message = "角色名不能为空")
    private String name;
    
    @NotBlank(message = "角色描述不能为空")
    private String description;
    
    @NotNull(message = "父ID不能为空")
    private Integer parentNodeId;
}