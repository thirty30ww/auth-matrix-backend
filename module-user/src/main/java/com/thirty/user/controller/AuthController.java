package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.constant.AuthConstant;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.vo.JwtVO;
import com.thirty.user.model.dto.LoginDTO;
import com.thirty.user.model.dto.RegisterDTO;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.service.UserService;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * 权限认证
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResultDTO<String> register(@RequestBody RegisterDTO registerDTO) {
        if (userService.validateUserExists(registerDTO.getUsername())) {
            return ResultDTO.of(AuthResultCode.USERNAME_EXISTS);
        }

        userService.createUser(registerDTO);
        
        return ResultDTO.of(AuthResultCode.REGISTER_SUCCESS);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultDTO<JwtVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );
        
        // 更新SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成访问令牌和刷新令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        
        // 返回JWT响应
        return ResultDTO.of(AuthResultCode.LOGIN_SUCCESS, new JwtVO(accessToken, refreshToken, loginDTO.getUsername()));
    }
    
    /**
     * 刷新令牌
     * 当访问令牌过期, 服务器返回401, 客户端自动拦截并调用此接口
     */
    @GetMapping("/refresh")
    public ResultDTO<JwtVO> refreshToken(@RequestParam String refreshToken) {
        // 验证刷新令牌
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            return ResultDTO.of(AuthResultCode.INVALID_REFRESH_TOKEN);
        }

        // 检查刷新令牌是否在黑名单中
        if (jwtUtil.isRefreshTokenInBlacklist(refreshToken)) {
            return ResultDTO.of(AuthResultCode.REFRESH_TOKEN_BLACKLISTED);
        }

        // 验证令牌是否过期
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResultDTO.of(AuthResultCode.REFRESH_TOKEN_EXPIRED);
        }

        // 从令牌中提取用户名
        String username = jwtUtil.extractUsername(refreshToken);

        // 加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!userDetails.isEnabled()) {
            return ResultDTO.of(AuthResultCode.USER_BANNED);
        }

        // 生成新的访问令牌和刷新令牌
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 将旧的刷新令牌加入黑名单（防止刷新令牌被重复使用）
        jwtUtil.addRefreshTokenToBlacklist(refreshToken);

        return ResultDTO.success(new JwtVO(newAccessToken, newRefreshToken, username));
    }

    /**
     * 退出登录
     * 将当前访问令牌和对应的刷新令牌都加入Redis黑名单，使其立即失效
     * 同时清除SecurityContext
     */
    @GetMapping("/logout")
    public ResultDTO<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestParam(required = false) String refreshToken) {
        // 处理访问令牌
        if (authHeader != null && authHeader.startsWith(AuthConstant.BEARER_PREFIX)) {
            String accessToken = authHeader.substring(AuthConstant.BEARER_PREFIX_LENGTH);
            if (jwtUtil.isAccessToken(accessToken)) {
                // 将访问令牌加入黑名单
                jwtUtil.addAccessTokenToBlacklist(accessToken);
            }

            // 处理刷新令牌（如果提供）
            if (refreshToken != null && !refreshToken.isEmpty()) {
                if (jwtUtil.isRefreshToken(refreshToken)) {
                    // 将刷新令牌加入黑名单
                    jwtUtil.addRefreshTokenToBlacklist(refreshToken);
                }
            }
        }
        
        // 清除SecurityContext
        SecurityContextHolder.clearContext();
        return ResultDTO.of(AuthResultCode.LOGOUT_SUCCESS);
    }
}