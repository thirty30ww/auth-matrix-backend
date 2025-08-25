package com.thirty.user.service.domain;

import com.thirty.user.model.dto.AddUserDTO;
import com.thirty.user.model.dto.ChangePasswordDTO;
import com.thirty.user.model.dto.ModifyUserDTO;
import com.thirty.user.model.dto.UpdateUserDTO;

import java.util.List;

public interface UserOperationDomain {
    /**
     * 创建用户
     * @param addUserDTO 用户信息
     * @return 用户ID
     */
    Integer addUser(AddUserDTO addUserDTO);

    /**
     * 修改用户
     * @param modifyUserDTO 修改用户请求参数
     */
    void modifyUser(ModifyUserDTO modifyUserDTO);

    /**
     * 更新用户
     * @param updateUserDTO 更新用户请求参数
     */
    void updateUser(UpdateUserDTO updateUserDTO);

    /**
     * 封禁用户
     * @param userIds 用户ID列表
     */
    void banUsers(List<Integer> userIds);

    /**
     * 解封用户
     * @param userIds 用户ID列表
     */
    void unbanUsers(List<Integer> userIds);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param changePasswordDTO 修改密码请求参数
     */
    void changePassword(Integer userId, ChangePasswordDTO changePasswordDTO);

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    void logout(String accessToken, String refreshToken);
}
