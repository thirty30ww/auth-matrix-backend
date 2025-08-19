package com.thirty.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.thirty.common.enums.result.GlobalResultCode;
import com.thirty.common.model.dto.ResultDTO;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

import static com.thirty.common.utils.exceptionUtil.getFirstFieldErrorByOrder;

/**
 * 全局异常处理
 * 所有异常处理方法统一返回200状态码，真实状态码在返回体中
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResultDTO<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return ResultDTO.failure(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数验证异常
     * 按照字段在类中的声明顺序返回第一个错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO<?> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        
        String errorMessage;
        if (fieldErrors.isEmpty()) {
            errorMessage = GlobalResultCode.PARAM_ERROR.getMessage();
        } else {
            // 按照字段在类中的声明顺序排序
            FieldError firstError = getFirstFieldErrorByOrder(fieldErrors, e.getBindingResult().getTarget());
            errorMessage = firstError.getDefaultMessage();
            errorMessage = StringUtils.isNotBlank(errorMessage) ? errorMessage : GlobalResultCode.PARAM_ERROR.getMessage();
        }

        return ResultDTO.failure(GlobalResultCode.PARAM_ERROR.getCode(), errorMessage);
    }
    
    /**
     * 处理JSON反序列化异常（包括枚举值不匹配等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultDTO<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // 检查是否是枚举值格式异常
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException ife) {
            // 提取字段名和无效值
            String fieldName = ife.getPath().isEmpty() ? "未知字段" : ife.getPath().get(ife.getPath().size() - 1).getFieldName();
            String invalidValue = ife.getValue() != null ? ife.getValue().toString() : "null";
            log.warn("参数格式异常：字段[{}]，值[{}]", fieldName, invalidValue);
        }
        
        return ResultDTO.of(GlobalResultCode.ENUM_ERROR);
    }
    
    /**
     * 处理404异常（接口不存在）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultDTO<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("请求的接口不存在: {}", e.getRequestURL());
        return ResultDTO.of(GlobalResultCode.INTERFACE_NOT_EXIST);
    }
    
    /**
     * 处理请求方法不支持异常（例如：应该用GET请求，但使用了POST请求）
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultDTO<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResultDTO.of(GlobalResultCode.METHOD_NOT_ALLOWED);
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResultDTO<?> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return ResultDTO.failure();
    }
}