package com.thirty.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtVO {
    private String accessToken;
    private String refreshToken;
    private String username;
}