package com.thirty.user.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirty.common.annotation.OperateLog;
import com.thirty.common.annotation.OperateModule;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.constant.JwtConstant;
import com.thirty.common.enums.model.MethodType;
import com.thirty.user.model.entity.LogOperation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class LogUtil {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 处理返回结果和状态码，记录操作时间
     * @param logOperation 日志对象
     * @param result 目标方法的返回结果
     * @throws JsonProcessingException JSON处理异常
     * @param startTime 方法执行开始时间
     */
    public void handleResult(LogOperation logOperation, Object result, Long startTime) throws JsonProcessingException {
        // 记录操作时间
        long endTime = System.currentTimeMillis();
        logOperation.setOperateTime((int) (endTime - startTime));

        // 记录返回结果
        if (result instanceof ResultDTO<?> resultDTO) {

            logOperation.setCode(resultDTO.getCode());

            // 序列化完整的返回结果
            String responseParam = objectMapper.writeValueAsString(resultDTO);
            logOperation.setResponseParam(responseParam);

        } else {
            log.warn("返回参数格式错误, result: {}", result);
        }
    }

    /**
     * 处理注解信息
     * @param operateLog 操作日志注解对象
     * @param logOperation 日志对象
     * @param module 模块名称
     */
    public void handleAnnotation(OperateLog operateLog, LogOperation logOperation, String module) {
        logOperation.setModule(module)
                .setDescription(operateLog.description())
                .setType(operateLog.type());
    }

    /**
     * 处理请求信息
     * @param request HttpServletRequest对象
     * @param logOperation 日志对象
     * @param joinPoint 连接点对象
     * @throws JsonProcessingException JSON处理异常
     */
    public void handleRequest(HttpServletRequest request, LogOperation logOperation, ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        if (request != null) {
            // 获取用户Ip，Url，请求方法
            logOperation.setIp(getIpAddress(request))
                    .setUrl(request.getRequestURI())
                    .setMethod(MethodType.getByCode(request.getMethod().toUpperCase()));

            // 获取请求参数
            String requestParam = getRequestParams(request, joinPoint);
            logOperation.setRequestParam(requestParam);
        } else {
            log.warn("请求信息为空");
        }
    }

    /**
     * 获取当前请求的用户ID
     * @param request HttpServletRequest对象
     * @return 用户ID
     */
    public Integer getCurrentUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 从请求头中获取Authorization头
        String authHeader = request.getHeader(JwtConstant.JWT_HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(JwtConstant.BEARER_PREFIX)) {
            return null;
        }
        // 从Authorization头中提取用户ID
        return jwtUtil.getUserIdFromAuthHeader(authHeader);
    }

    /**
     * 获取请求的IP地址
     * @param request HttpServletRequest对象
     * @return IP地址
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求的参数
     * @param request HttpServletRequest对象
     * @param joinPoint 连接点对象
     * @return 请求参数
     * @throws JsonProcessingException JSON处理异常
     */
    public String getRequestParams(HttpServletRequest request, ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        MethodType method = MethodType.getByCode(request.getMethod().toUpperCase());

        // 如果是GET或DELETE方法，返回查询参数
        if (MethodType.getQueryMethodTypes().contains(method)) {
            return getQueryParams(request);
        }
        // 如果是POST、PUT或PATCH方法，返回请求体参数
        else if (MethodType.getBodyMethodTypes().contains(method)) {
            return getBodyParams(joinPoint);
        }
        // 其他方法类型，返回null
        return null;
    }

    /**
     * 获取模块名称
     * @param joinPoint 连接点
     * @return 模块名称
     */
    public String getModuleName(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        OperateModule operateModule = targetClass.getAnnotation(OperateModule.class);
        // 如果类上有@OperateModule注解，返回注解值；否则返回类名
        return operateModule != null ? operateModule.value() : targetClass.getSimpleName();
    }

    /**
     * 获取GET或DELETE方法的请求参数
     * @param request HttpServletRequest对象
     * @return 请求参数
     */
    private String getQueryParams(HttpServletRequest request) throws JsonProcessingException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.isEmpty()) {
            return null;
        }

        // 转换为Map<String, Object>
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            // 处理参数值
            String[] values = entry.getValue();
            if (values.length == 1) {
                params.put(entry.getKey(), values[0]);
            } else {
                params.put(entry.getKey(), values);
            }
        }

        return objectMapper.writeValueAsString(params);
    }

    /**
     * 获取POST、PUT或PATCH方法的请求参数
     * @param joinPoint 连接点对象
     * @return 请求参数
     * @throws JsonProcessingException JSON处理异常
     */
    private String getBodyParams(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return null;
        }

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();

        // 遍历参数，只记录 @RequestBody 标注的参数
        for (int i = 0; i < parameters.length && i < args.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];

            // 检查是否有 @RequestBody 注解
            if (parameter.isAnnotationPresent(org.springframework.web.bind.annotation.RequestBody.class) && arg != null) {
                return objectMapper.writeValueAsString(arg);
            }
        }

        return null;
    }
}
