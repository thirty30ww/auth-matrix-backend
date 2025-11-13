package com.thirty.user.aspect;

import com.thirty.common.annotation.OperateLog;
import com.thirty.common.utils.ExceptionUtil;
import com.thirty.common.utils.LogContext;
import com.thirty.user.model.entity.LogOperation;
import com.thirty.user.service.basic.LogOperationService;
import com.thirty.user.utils.LogUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class OperateLogAspect {
    @Resource
    private LogOperationService logOperationService;
    @Resource
    private LogUtil logUtil;
    @Resource
    private ExceptionUtil exceptionUtil;

    /**
     * 环绕通知，记录操作日志
     * 拦截标注了@OperateLog注解的方法
     *
     * @param joinPoint 连接点
     * @param operateLog 操作日志注解
     * @return 目标方法的返回结果
     * @throws Throwable 异常
     */
    @Around("@annotation(operateLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 创建日志对象
        LogOperation logOperation = new LogOperation();
        Object result;

        // 处理请求信息
        logUtil.handleRequest(request, logOperation, joinPoint);

        // 处理注解信息
        String module = logUtil.getModuleName(joinPoint);
        logUtil.handleAnnotation(operateLog, logOperation, module);

        // 记录用户ID
        Integer userId = logUtil.getCurrentUserId(request);
        logOperation.setUserId(userId);

        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 正常情况：直接记录成功日志
            logUtil.handleResult(logOperation, result, startTime);
            logOperationService.save(logOperation);

            return result;
        } catch (Exception e) {
            // 记录异常信息
            String errorMessage = exceptionUtil.buildDetailedErrorMessage(e, joinPoint);
            logOperation.setErrorMessage(errorMessage);

            // 设置ThreadLocal（确保异常处理器能获取到）
            LogContext.setLogContext(logOperation, startTime);

            // 让异常正常抛出，交给异常处理器
            throw e;
        }
    }


}
