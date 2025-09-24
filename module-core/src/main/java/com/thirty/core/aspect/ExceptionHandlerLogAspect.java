package com.thirty.core.aspect;

import com.thirty.common.utils.LogContext;
import com.thirty.user.model.entity.LogOperation;
import com.thirty.user.service.basic.LogOperationService;
import com.thirty.user.utils.LogUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionHandlerLogAspect {
    @Resource
    private LogOperationService logOperationService;

    @Resource
    private LogUtil logUtil;

    /**
     * 拦截所有异常处理器的方法
     * 匹配规则：
     * - GlobalExceptionHandler中的所有方法
     * - UserExceptionHandler中的所有方法
     * - 以及其他可能的XxxExceptionHandler
     */
    @Around("execution(* *..*ExceptionHandler.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行异常处理方法
        Object result = joinPoint.proceed();

        // 如果不需要记录日志的请求直接返回结果
        if (!LogContext.needLogOperation()) {
            return result;
        }

        try {
            // 获得日志操作对象
            Object logOperationObj = LogContext.getLogOperation();
            Long startTime = LogContext.getStartTime();

            if (logOperationObj instanceof LogOperation logOperation) {
                // 处理返回结果和状态码
                logUtil.handleResult(logOperation, result, startTime);

                // 保存日志
                logOperationService.save(logOperation);
            } else {
                log.warn("LogOperation对象类型不匹配: {}",
                        logOperationObj != null ? logOperationObj.getClass() : "null");
            }
        } finally {
            // 清理ThreadLocal，避免内存泄漏
            LogContext.clear();
        }

        return result;
    }
}
