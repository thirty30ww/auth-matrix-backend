package com.thirty.user.model.dto;

import com.thirty.user.annotation.ValidPassword;
import lombok.Data;

@Data
public class RegisterDTO {
    String username;
    @ValidPassword
    String password;
}
