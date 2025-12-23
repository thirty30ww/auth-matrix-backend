package com.thirty.user.service.basic;

public interface UserOnlineService {
    /**
     * 用户上线
     * @param userId 用户ID
     * @param sessionId 会话ID
     */
    void userOnline(String userId, String sessionId);

    /**
     * 用户下线
     * @param userId 用户ID
     * @param sessionId 会话ID
     */
    void userOffline(String userId, String sessionId);

    /**
     * 获取在线用户数量
     * @return 在线用户数量
     */
    int getOnlineCount();

    /**
     * 判断用户是否在线
     * @param userId 用户ID
     * @return 如果用户在线则返回true，否则返回false
     */
    boolean isUserOnline(String userId);
}
