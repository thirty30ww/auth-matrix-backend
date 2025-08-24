package com.thirty.user.service.domain.impl;

import com.thirty.user.model.entity.User;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.UserValidationDomain;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserValidationDomainImpl implements UserValidationDomain {

    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 校验用户是否存在
     * @param username 用户名
     * @return true 存在 false 不存在
     */
    @Override
    public boolean validateUserExists(String username) {
        return userService.validateUserExists(username);
    }


    /**
     * 校验用户ID是否匹配
     * @param username 用户名
     * @param userId 用户ID
     * @return true 匹配 false 不匹配
     */
    @Override
    public boolean validateUserIdMatch(String username, Integer userId) {
        User user = userService.getUser(username);
        return user.getId().equals(userId);
    }

    /**
     * 校验新密码与确认密码是否匹配
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     * @return true 匹配 false 不匹配
     */
    @Override
    public boolean validateNewPasswordAndConfirmPassword(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }

    /**
     * 校验密码是否正确
     * @param currentUsername 当前用户名
     * @param currentPassword 当前密码
     * @return true 正确 false 错误
     */
    @Override
    public boolean validatePassword(String currentUsername, String currentPassword) {
        User user = userService.getUser(currentUsername);
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }
}
