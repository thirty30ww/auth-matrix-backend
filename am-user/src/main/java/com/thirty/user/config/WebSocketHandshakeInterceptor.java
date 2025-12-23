package com.thirty.user.config;

import com.thirty.user.constant.JwtConstant;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.jspecify.annotations.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket握手拦截器
 * 用于在握手阶段验证JWT令牌
 */
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,
                                   @NonNull Map<String, Object> attributes) {
        // 从请求头中获取JWT令牌
        String authHeader = request.getHeaders().getFirst(JwtConstant.JWT_HEADER_NAME);
        String token = jwtUtil.extractToken(authHeader);
        // 验证访问令牌
        if (jwtUtil.isAccessToken(token) && !jwtUtil.isTokenExpired(token)) {
            Integer userId = jwtUtil.extractUserId(token);
            // 将用户ID存储到属性中，后续可以在WebSocketHandler中使用
            attributes.put("userId", userId);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
                               @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler,
                               Exception exception) {
        // 握手完成后的操作，可留空
    }
}
