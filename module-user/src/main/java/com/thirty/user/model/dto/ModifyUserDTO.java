package com.thirty.user.model.dto;

import com.thirty.user.enums.model.UserSex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ModifyUserDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer id;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotNull(message = "性别不能为空")
    private UserSex sex;
    @NotEmpty(message = "角色不能为空")
    private List<Integer> roleIds;
}
