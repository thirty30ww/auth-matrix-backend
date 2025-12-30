package com.thirty.common.aspect;

import com.thirty.common.annotation.RateLimiter;
import com.thirty.common.constant.JwtConstant;
import com.thirty.common.enums.result.GlobalResultCode;
import com.thirty.common.exception.BusinessException;
import com.thirty.common.utils.JwtUtil;
import com.thirty.common.utils.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 * <li>方法级别注解优先
 * <li>类级别注解作为默认值
 */
@Aspect
@Component
@Slf4j
public class RateLimiterAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtUtil jwtUtil;

    @Pointcut("@within(com.thirty.common.annotation.RateLimiter) || @annotation(com.thirty.common.annotation.RateLimiter)")
    public void rateLimiterPointcut() {
    }

    /**
     * 前置通知，检查是否超过限流次数
     *
     * @param point 连接点
     */
    @Before("rateLimiterPointcut()")
    public void doBefore(JoinPoint point) {
        RateLimiter methodRateLimiter = getMethodAnnotation(point); // 获取方法上的注解
        RateLimiter classRateLimiter = getClassAnnotation(point);   // 获取类上的注解

        // 确定使用的注解（方法级别注解优先）
        RateLimiter rateLimiter = methodRateLimiter != null ? methodRateLimiter : classRateLimiter;

        if (rateLimiter == null) {
            return;
        }

        String key = getCombineKey(rateLimiter, point);

        // 使用Redis的原子操作实现限流
        Long num = redisTemplate.opsForValue().increment(key);
        if (num != null && num == 1) {
            // 第一次访问，设置过期时间
            redisTemplate.expire(key, rateLimiter.time(), TimeUnit.SECONDS);
        }

        if (num != null && num > rateLimiter.count()) {
            // 超过限流次数
            throw new BusinessException(GlobalResultCode.RATE_LIMIT_EXCEEDED, rateLimiter.message());
        }
    }

    /**
     * 获取方法上的@RateLimiter注解
     */
    private RateLimiter getMethodAnnotation(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(RateLimiter.class);
    }

    /**
     * 获取类上的@RateLimiter注解
     */
    private RateLimiter getClassAnnotation(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        return targetClass.getAnnotation(RateLimiter.class);
    }

    /**
     * 获取限流key
     * 限流key的格式为：rate_limit:limitType:key
     * <li>limitType为IP时，key为IP地址
     * <li>limitType为TOKEN时，key为用户ID（从Token中获取）
     * <li>limitType为CUSTOM时，key为注解中指定的key
     * <li>limitType为DEFAULT时，key为方法全路径（类名+方法名）
     *
     * @param rateLimiter 限流注解
     * @param point 连接点
     * @return 限流key
     */
    private String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuilder stringBuffer = new StringBuilder("rate_limit:");
        String methodInfo = getMethodInfo(point);

        // 根据限流类型生成不同的key
        switch (rateLimiter.limitType()) {
            case IP:
                stringBuffer.append("ip:").append(WebUtil.getIpAddress());
                break;

            case TOKEN:
                Integer userId = getUserIdFromToken();
                if (userId != null) {
                    stringBuffer.append("token:").append(userId);
                } else {
                    // 没有token，使用IP作为fallback
                    stringBuffer.append("ip:").append(WebUtil.getIpAddress());
                }
                break;

            case CUSTOM:
                // 自定义key
                stringBuffer.append(rateLimiter.key());
                break;

            case DEFAULT:
                stringBuffer.append("method");
                break;
        }
        // 追加方法信息
        stringBuffer.append(":").append(methodInfo);
        return stringBuffer.toString();
    }

    /**
     * 从请求中获取用户ID（从Token中获取）
     */
    private Integer getUserIdFromToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 从Header获取Token
        String authHeader = request.getHeader(JwtConstant.JWT_HEADER_NAME);
        try {
            return jwtUtil.getUserIdFromAuthHeader(authHeader);
        } catch (Exception e) { // 没有authHeader，返回null
            return null;
        }
    }

    /**
     * 获取方法信息（类名:方法名）
     * @param point 连接点
     * @return 方法信息（类名:方法名）
     */
    private String getMethodInfo(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        return targetClass.getName() + ":" + method.getName();
    }
}