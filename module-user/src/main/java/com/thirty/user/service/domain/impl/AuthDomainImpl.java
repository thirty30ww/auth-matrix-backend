package com.thirty.user.service.domain.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.model.vo.JwtVO;
import com.thirty.user.service.domain.AuthDomain;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthDomainImpl implements AuthDomain {
    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param authentication 认证信息
     * @return JwtVO
     */
    @Override
    public JwtVO login(String username, String password, Authentication authentication) {
        // 生成访问令牌和刷新令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 生成JwtVO
        return new JwtVO(accessToken, refreshToken, username);
    }

    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return JwtVO
     */
    @Override
    public JwtVO refreshToken(String refreshToken) {
        // 验证刷新令牌
        validateRefreshToken(refreshToken);

        // 从令牌中提取用户名
        String username = jwtUtil.extractUsername(refreshToken);

        // 加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 检查用户是否被禁用
        if (!userDetails.isEnabled()) {
            throw new BusinessException(AuthResultCode.USER_BANNED);
        }

        // 生成新的访问令牌和刷新令牌
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 将旧的刷新令牌加入黑名单（防止刷新令牌被重复使用）
        jwtUtil.addRefreshTokenToBlacklist(refreshToken);

        // 生成JwtVO
        return new JwtVO(newAccessToken, newRefreshToken, username);
    }

    /**
     * 验证刷新令牌
     * @param refreshToken 刷新令牌
     */
    private void validateRefreshToken(String refreshToken) {
        // 验证刷新令牌
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(AuthResultCode.INVALID_REFRESH_TOKEN);
        }

        // 检查刷新令牌是否在黑名单中
        if (jwtUtil.isRefreshTokenInBlacklist(refreshToken)) {
            throw new BusinessException(AuthResultCode.REFRESH_TOKEN_BLACKLISTED);
        }

        // 验证令牌是否过期
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new BusinessException(AuthResultCode.REFRESH_TOKEN_EXPIRED);
        }
    }
}
