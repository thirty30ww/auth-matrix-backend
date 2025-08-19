package com.thirty.user.exception;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.AuthResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 用户模块异常处理
 * 处理用户模块特有的认证和业务异常
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class UserExceptionHandler {
    /**
     * 处理用户不存在异常
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResultDTO<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("用户名不存在异常: {}", e.getMessage());
        return ResultDTO.of(AuthResultCode.USERNAME_NOT_EXISTS);
    }

    /**
     * 处理用户账户被禁用异常
     */
    @ExceptionHandler(DisabledException.class)
    public ResultDTO<?> handleDisabledException(DisabledException e) {
        log.warn("用户账户被禁用异常: {}", e.getMessage());
        return ResultDTO.of(AuthResultCode.USER_BANNED);
    }

    /**
     * 处理密码错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResultDTO<?> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("密码错误异常: {}", e.getMessage());
        return ResultDTO.of(AuthResultCode.INVALID_CREDENTIALS);
    }

}
