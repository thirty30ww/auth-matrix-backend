package com.thirty.user.aspect;

import com.thirty.common.enums.model.Status;
import com.thirty.common.utils.LogContext;
import com.thirty.user.annotation.LoginLog;
import com.thirty.user.constant.JwtConstant;
import com.thirty.user.enums.model.LoginType;
import com.thirty.user.model.entity.LogLogin;
import com.thirty.user.service.basic.LogLoginService;
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
public class LoginLogAspect {
    @Resource
    private LogLoginService logLoginService;

    @Resource
    private LogUtil logUtil;

    /**
     * 环绕通知，记录登录日志
     * 拦截标注了@LoginLog注解的方法
     *
     * @param joinPoint 连接点
     * @param loginLog 登录日志注解
     * @return 目标方法的返回结果
     * @throws Throwable 异常
     */
    @Around("@annotation(loginLog)")
    public Object around(ProceedingJoinPoint joinPoint, LoginLog loginLog) throws Throwable {
        // 记录开始时间，用于计算操作耗时
        long startTime = System.currentTimeMillis();

        // 获取HTTP请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 创建基础登录日志
        LogLogin logLoginEntity = logUtil.createBaseLoginLog(request, loginLog.type());

        // 获取当前登录用户ID
        Integer userId = getCurrentUserIdByType(loginLog.type(), joinPoint, request);
        logLoginEntity.setUserId(userId);

        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            // 操作成功，设置状态为SUCCESS
            logLoginEntity.setStatus(Status.SUCCESS);

            // 记录操作耗时
            logLoginEntity.setOperateTime((int) (System.currentTimeMillis() - startTime));

            // 保存登录日志
            logLoginService.save(logLoginEntity);

            return result;
        } catch (Exception e) {
            // 操作失败，设置状态为FAILED，并记录异常信息
            logLoginEntity.setStatus(Status.FAILED);

            // 设置ThreadLocal（确保异常处理器能获取到登录日志）
            LogContext.setLogContext(logLoginEntity, startTime);
            throw e;
        }
    }

    /**
     * 获取当前登录用户ID，根据登录类型不同，从不同的位置获取用户ID
     *
     * @param loginType 登录类型
     * @param joinPoint 连接点
     * @param request   HTTP请求对象
     * @return 当前登录用户ID
     */
    private Integer getCurrentUserIdByType(LoginType loginType, ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        try {
            return switch (loginType) {
                case LOGIN -> logUtil.getCurrentUserId(joinPoint.getArgs());
                case LOGOUT -> logUtil.getCurrentUserId(request);
                case REFRESH -> logUtil.getCurrentUserId(request.getParameter(JwtConstant.REFRESH_TOKEN));
                case REGISTER -> null;
            };
        } catch (Exception e) {
            // JWT解析失败时，记录调试日志但不影响日志记录流程
            log.info("accessToken无效，无法获取当前用户ID记录登录日志");
            return null;
        }
    }
}
