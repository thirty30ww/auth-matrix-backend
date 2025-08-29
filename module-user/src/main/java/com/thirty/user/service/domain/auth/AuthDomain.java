package com.thirty.user.service.domain.auth;

import com.thirty.user.model.vo.JwtVO;
import org.springframework.security.core.Authentication;

public interface AuthDomain {
    /**
     * 用户登录
     * @param username 用户名
     * @param authentication 认证信息
     * @return JwtVO
     */
    JwtVO login(String username, Authentication authentication);

    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return JwtVO
     */
    JwtVO refreshToken(String refreshToken);
}
