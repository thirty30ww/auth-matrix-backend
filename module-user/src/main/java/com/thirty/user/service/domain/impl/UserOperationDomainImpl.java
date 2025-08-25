package com.thirty.user.service.domain.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.user.converter.UserDtoConverter;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.dto.AddUserDTO;
import com.thirty.user.model.dto.ChangePasswordDTO;
import com.thirty.user.model.dto.ModifyUserDTO;
import com.thirty.user.model.dto.UpdateUserDTO;
import com.thirty.user.model.entity.Detail;
import com.thirty.user.model.entity.User;
import com.thirty.user.service.basic.DetailService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.UserOperationDomain;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOperationDomainImpl implements UserOperationDomain {
    @Resource
    private UserService userService;
    @Resource
    private DetailService detailService;
    @Resource
    private UserRoleService userRoleService;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtUtil jwtUtil;


    /**
     * 创建用户
     * @param addUserDTO 用户信息
     * @return 用户ID
     */
    @Override
    public Integer addUser(AddUserDTO addUserDTO) {
        // 新建用户
        String encodedPassword = passwordEncoder.encode(addUserDTO.getPassword());
        Integer userId = userService.createUser(addUserDTO.getUsername(), encodedPassword);

        // 绑定用户详情
        Detail detail = UserDtoConverter.INSTANCE.addUserDTOToDetail(addUserDTO);
        detailService.createDetail(userId, detail);

        // 关联用户角色
        userRoleService.addUserRoles(userId, addUserDTO.getRoleIds());

        return userId;
    }

    /**
     * 修改用户
     * @param modifyUserDTO 修改用户请求参数
     */
    @Override
    public void modifyUser(ModifyUserDTO modifyUserDTO) {
        // 校验用户是否存在
        User user = userService.getById(modifyUserDTO.getId());
        if (user == null) {
            throw new BusinessException(UserResultCode.USER_NOT_EXISTS);
        }

        // 转换为Detail对象
        Detail detail = UserDtoConverter.INSTANCE.modifyUserDTOToDetail(modifyUserDTO);

        // 更新Detail信息
        detailService.updateById(detail);

        // 更新用户角色
        userRoleService.updateUserRoles(user.getId(), modifyUserDTO.getRoleIds());
    }

    /**
     * 更新用户
     * @param updateUserDTO 更新用户请求参数
     */
    @Override
    public void updateUser(UpdateUserDTO updateUserDTO) {
        // 转换为Detail对象
        Detail detail = UserDtoConverter.INSTANCE.updateUserDTOToDetail(updateUserDTO);

        // 更新Detail信息
        detailService.updateById(detail);
    }

    /**
     * 封禁用户
     * @param userIds 用户ID列表
     */
    @Override
    public void banUsers(List<Integer> userIds) {
        // 执行封禁
        userService.banUsers(userIds);
    }

    /**
     * 解封用户
     * @param userIds 用户ID列表
     */
    @Override
    public void unbanUsers(List<Integer> userIds) {
        // 执行解封
        userService.unbanUsers(userIds);
    }

    /**
     * 修改密码
     * @param userId 用户ID
     * @param changePasswordDTO 修改密码请求参数
     */
    @Override
    public void changePassword(Integer userId, ChangePasswordDTO changePasswordDTO) {
        // 获取用户
        User user = userService.getById(userId);

        // 加密并更新密码
        String encodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
        userService.updateById(user.setPassword(encodedPassword));
    }

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    @Override
    public void logout(String accessToken, String refreshToken) {
        // 处理访问令牌
        jwtUtil.addAccessTokenToBlacklist(accessToken);

        // 处理刷新令牌（如果提供）
        if (refreshToken != null && !refreshToken.isEmpty()) {
            if (jwtUtil.isRefreshToken(refreshToken)) {
                // 将刷新令牌加入黑名单
                jwtUtil.addRefreshTokenToBlacklist(refreshToken);
            }
        }
    }
}
