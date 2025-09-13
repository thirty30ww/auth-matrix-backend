package com.thirty.user.service.domain.auth.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.JwtVO;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.auth.AuthDomain;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthDomainImpl implements AuthDomain {
    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * @param username 用户名
     * @param authentication 认证信息
     * @return JwtVO
     */
    @Override
    public JwtVO login(String username, Authentication authentication) {
        // 生成访问令牌和刷新令牌
        User user = userService.getUser(username);
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

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

        String username = jwtUtil.extractUsername(refreshToken);    // 从令牌中提取用户名
        User user = userService.getUser(username);                  // 加载用户详情

        // 检查用户是否被禁用
        if (!user.getIsValid()) {
            throw new BusinessException(AuthResultCode.USER_BANNED);
        }

        // 生成新访问令牌和刷新令牌
        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

        // 将旧刷新令牌加入黑名单（防止刷新令牌被重复使用）
        jwtUtil.addRefreshTokenToBlacklist(refreshToken);

        // 生成JwtVO
        return new JwtVO(newAccessToken, newRefreshToken, username);
    }

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    @Override
    public void logout(String accessToken, String refreshToken) {
        // 处理访问令牌
        jwtUtil.addAccessTokenToBlacklist(accessToken);

        // 处理刷新令牌（如果提供）
        if (refreshToken != null && !refreshToken.isEmpty()) {
            if (jwtUtil.isRefreshToken(refreshToken)) {
                // 将刷新令牌加入黑名单
                jwtUtil.addRefreshTokenToBlacklist(refreshToken);
            }
        }
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
