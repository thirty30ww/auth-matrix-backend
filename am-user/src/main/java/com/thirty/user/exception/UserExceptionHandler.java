package com.thirty.user.exception;

import com.thirty.common.enums.result.GlobalResultCode;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.common.enums.result.AuthResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
     * 处理Spring Security方法级权限验证异常
     * 当@PreAuthorize注解校验失败时触发
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResultDTO<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return ResultDTO.of(GlobalResultCode.FORBIDDEN);
    }

    /**
     * 处理用户不存在异常
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResultDTO<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResultDTO.of(AuthResultCode.USERNAME_NOT_EXISTS);
    }

    /**
     * 处理用户账户被禁用异常
     */
    @ExceptionHandler(DisabledException.class)
    public ResultDTO<?> handleDisabledException(DisabledException e) {
        return ResultDTO.of(AuthResultCode.USER_BANNED);
    }

    /**
     * 处理密码错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResultDTO<?> handleBadCredentialsException(BadCredentialsException e) {
        return ResultDTO.of(AuthResultCode.INVALID_CREDENTIALS);
    }

}
