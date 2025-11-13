package com.thirty.user.model.dto;

import com.thirty.user.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    String username;
    @ValidPassword
    String password;

    String confirmPassword;
}
