package com.thirty.user.service.domain;

public interface UserValidationDomain {
    /**
     * 校验用户是否存在
     * @param username 用户名
     * @return true 存在 false 不存在
     */
    boolean validateUserExists(String username);

    /**
     * 校验用户ID是否匹配
     * @param username 用户名
     * @param userId 用户ID
     * @return true 匹配 false 不匹配
     */
    boolean validateUserIdMatch(String username, Integer userId);

    /**
     * 校验新密码与确认密码是否匹配
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     * @return true 匹配 false 不匹配
     */
    boolean validateNewPasswordAndConfirmPassword(String newPassword, String confirmPassword);


    /**
     * 校验密码是否正确
     * @param currentUsername 当前用户名
     * @param currentPassword 当前密码
     * @return true 正确 false 错误
     */
    boolean validatePassword(String currentUsername, String currentPassword);
}
