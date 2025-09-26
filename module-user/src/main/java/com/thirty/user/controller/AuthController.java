package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.annotation.LoginLog;
import com.thirty.user.enums.model.LoginType;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.model.dto.LoginDTO;
import com.thirty.user.model.vo.JwtVO;
import com.thirty.user.service.facade.AuthFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 权限认证
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthFacade authFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @LoginLog(type = LoginType.LOGIN)
    public ResultDTO<JwtVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResultDTO.of(AuthResultCode.LOGIN_SUCCESS, authFacade.login(loginDTO));
    }
    
    /**
     * 刷新令牌
     * 当访问令牌过期, 服务器返回401, 客户端自动拦截并调用此接口
     */
    @GetMapping("/refresh")
    @LoginLog(type = LoginType.REFRESH)
    public ResultDTO<JwtVO> refreshToken(@RequestParam String refreshToken) {
        return ResultDTO.of(AuthResultCode.LOGIN_SUCCESS, authFacade.refreshToken(refreshToken));
    }

    /**
     * 退出登录
     * 将当前访问令牌和对应的刷新令牌都加入Redis黑名单，使其立即失效
     * 同时清除SecurityContext
     */
    @GetMapping("/logout")
    @LoginLog(type = LoginType.LOGOUT)
    public ResultDTO<Void> logout(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(required = false) String refreshToken) {
        String accessToken = jwtUtil.extractToken(authHeader);
        authFacade.logout(accessToken, refreshToken);
        return ResultDTO.of(AuthResultCode.LOGOUT_SUCCESS);
    }
}