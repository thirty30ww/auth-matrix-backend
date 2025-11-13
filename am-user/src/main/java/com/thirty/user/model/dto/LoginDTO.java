package com.thirty.user.model.dto;

import com.thirty.user.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @ValidPassword
    private String password;
}