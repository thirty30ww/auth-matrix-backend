package com.thirty.user.service.domain;

import com.thirty.user.model.vo.JwtVO;
import org.springframework.security.core.Authentication;

public interface AuthDomain {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param authentication 认证信息
     * @return JwtVO
     */
    JwtVO login(String username, String password, Authentication authentication);

    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return JwtVO
     */
    JwtVO refreshToken(String refreshToken);
}
