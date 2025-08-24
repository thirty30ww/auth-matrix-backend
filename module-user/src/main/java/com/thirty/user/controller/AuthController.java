package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.model.dto.LoginDTO;
import com.thirty.user.model.vo.JwtVO;
import com.thirty.user.service.facade.AuthFacade;
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

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultDTO<JwtVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResultDTO.of(AuthResultCode.LOGIN_SUCCESS, authFacade.login(loginDTO));
    }
    
    /**
     * 刷新令牌
     * 当访问令牌过期, 服务器返回401, 客户端自动拦截并调用此接口
     */
    @GetMapping("/refresh")
    public ResultDTO<JwtVO> refreshToken(@RequestParam String refreshToken) {
        return ResultDTO.of(AuthResultCode.LOGIN_SUCCESS, authFacade.refreshToken(refreshToken));
    }
}