package com.thirty.user.config;

import com.thirty.user.service.basic.UserOnlineService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

/**
 * WebSocket事件监听器
 * 用于处理WebSocket连接和断开事件
 */
@Component
public class WebSocketEvenListener {
    @Resource
    private UserOnlineService  userOnlineService;

    /**
     * 处理WebSocket连接事件
     * @param event 连接事件
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        String userId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("userId");

        if (userId != null) {
            userOnlineService.userOnline(userId, sessionId);
        }
    }

    /**
     * 处理WebSocket断开事件
     * @param event 断开事件
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        String userId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("userId");

        if (userId != null) {
            userOnlineService.userOffline(userId, sessionId);
        }
    }
}
