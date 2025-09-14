package com.thirty.user.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignViewDTO {
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;
    @NotEmpty(message = "视图ID列表不能为空")
    private List<Integer> viewIds;
}
