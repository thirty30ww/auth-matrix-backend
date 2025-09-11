package com.thirty.user.service.facade.impl;

import com.thirty.user.model.dto.LoginDTO;
import com.thirty.user.model.vo.JwtVO;
import com.thirty.user.service.domain.auth.AuthDomain;
import com.thirty.user.service.facade.AuthFacade;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthFacadeImpl implements AuthFacade {
    @Resource
    private AuthDomain authDomain;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 用户登录
     * @param loginDTO 登录DTO
     * @return JwtVO
     */
    @Override
    public JwtVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        // 更新SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JwtVO
        return authDomain.login(username, authentication);
    }

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return JwtVO
     */
    @Override
    public JwtVO refreshToken(String refreshToken) {
        return authDomain.refreshToken(refreshToken);
    }

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    @Override
    public void logout(String accessToken, String refreshToken) {
        // 将令牌加入黑名单
        authDomain.logout(accessToken, refreshToken);
        // 清除SecurityContext
        SecurityContextHolder.clearContext();
    }
}
