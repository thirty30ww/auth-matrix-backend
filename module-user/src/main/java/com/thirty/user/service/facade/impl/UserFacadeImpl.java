package com.thirty.user.service.facade.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.system.api.SettingApi;
import com.thirty.common.exception.BusinessException;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.domain.role.builder.RolesBuilderFactory;
import com.thirty.user.service.domain.role.builder.RoleValidationBuilderFactory;
import com.thirty.user.service.domain.user.UserOperationDomain;
import com.thirty.user.service.domain.user.UserQueryDomain;
import com.thirty.user.service.domain.user.UserValidationDomain;
import com.thirty.user.service.facade.UserFacade;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserValidationDomain userValidationDomain;
    @Resource
    private UserOperationDomain userOperationDomain;
    @Resource
    private UserQueryDomain userQueryDomain;
    @Resource
    private RolesBuilderFactory rolesBuilderFactory;
    @Resource
    private RoleValidationBuilderFactory roleValidationBuilderFactory;

    @Resource
    private SettingApi settingApi;

    /**
     * 添加用户
     * @param operatorUserId 操作人用户ID
     * @param addUserDTO 添加用户请求参数
     */
    @Override
    public void addUser(Integer operatorUserId, AddUserDTO addUserDTO) {
        // 校验用户是否存在
        if (userValidationDomain.validateUserExists(addUserDTO.getUsername())) {
            throw new BusinessException(UserResultCode.USERNAME_ALREADY_EXISTS);
        }
        // 对操作人是否有权限添加用户做角色校验
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(operatorUserId).validateRoles(addUserDTO.getRoleIds())) {
            throw new BusinessException(UserResultCode.ROLE_NOT_AUTHORIZED_ADD);
        }
        userOperationDomain.addUser(addUserDTO);
    }

    /**
     * 获取用户
     * @param userId 用户ID
     * @return 用户VO
     */
    @Override
    public UserVO getUser(Integer userId) {
        return userQueryDomain.getUser(userId);
    }

    /**
     * 修改用户
     * @param operatorUserId 操作人用户ID
     * @param modifyUserDTO 修改用户请求参数
     */
    @Override
    public void modifyUser(Integer operatorUserId, ModifyUserDTO modifyUserDTO) {
        // 对操作人是否有权限修改用户做角色校验
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(operatorUserId).validateUser(modifyUserDTO.getId())) {
            throw new BusinessException(UserResultCode.USER_NOT_AUTHORIZED_MODIFY);
        }
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(operatorUserId).validateRoles(modifyUserDTO.getRoleIds())) {
            throw new BusinessException(UserResultCode.ROLE_NOT_AUTHORIZED_MODIFY);
        }
        userOperationDomain.modifyUser(modifyUserDTO);
    }

    /**
     * 更新用户
     * @param operatorUserId 操作人用户ID
     * @param updateUserDTO 更新用户请求参数
     */
    @Override
    public void updateUser(Integer operatorUserId, UpdateUserDTO updateUserDTO) {
        // 校验用户ID是否匹配
        if (!userValidationDomain.validateUserIdMatch(operatorUserId, updateUserDTO.getId())) {
            throw new BusinessException(UserResultCode.USER_ID_MISMATCH);
        }
        userOperationDomain.updateUser(updateUserDTO);
    }

    /**
     * 封禁用户
     * @param operatorUserId 操作人用户ID
     * @param userIds 用户ID列表
     */
    @Override
    public void banUsers(Integer operatorUserId, List<Integer> userIds) {
        // 对操作人是否有权限封禁用户做角色校验
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(operatorUserId).validateUsers(userIds)) {
            throw new BusinessException(UserResultCode.ROLE_NOT_AUTHORIZED_BAN);
        }
        userOperationDomain.banUsers(userIds);
    }

    /**
     * 解封用户
     * @param operatorUserId 操作人用户ID
     * @param userIds 用户ID列表
     */
    @Override
    public void unbanUsers(Integer operatorUserId, List<Integer> userIds) {
        // 对操作人是否有权限解封用户做角色校验
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(operatorUserId).validateUsers(userIds)) {
            throw new BusinessException(UserResultCode.ROLE_NOT_AUTHORIZED_UNBAN);
        }
        userOperationDomain.unbanUsers(userIds);
    }

    /**
     * 修改密码
     * @param userId 用户名
     * @param changePasswordDTO 修改密码请求参数
     */
    @Override
    public void changePassword(Integer userId, ChangePasswordDTO changePasswordDTO) {
        // 校验新密码与确认密码是否匹配
        if (!userValidationDomain.validateNewPasswordAndConfirmPassword(changePasswordDTO.getNewPassword(), changePasswordDTO.getConfirmPassword())) {
            throw new BusinessException(UserResultCode.PASSWORD_MISMATCH);
        }
        // 校验当前密码是否正确
        if (!userValidationDomain.validatePassword(userId, changePasswordDTO.getCurrentPassword())) {
            throw new BusinessException(UserResultCode.CURRENT_PASSWORD_INCORRECT);
        }

        userOperationDomain.changePassword(userId, changePasswordDTO);
    }

    /**
     * 获取用户列表
     * @param currentUserId 当前用户名
     * @param pageQueryDTO 获取用户列表请求参数
     * @return 用户列表
     */
    @Override
    public IPage<UserVO> getUsers(Integer currentUserId, PageQueryDTO<GetUsersDTO> pageQueryDTO) {
        // 获取当前用户有权限的角色ID列表
        List<Integer> permittedRoleIds = rolesBuilderFactory.createWithChildAndGlobal(currentUserId)
            .buildIds();

        // 获取用户列表
        return userQueryDomain.getUsers(pageQueryDTO, permittedRoleIds, settingApi.hasPermissionDisplay());
    }

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    @Override
    public void logout(String accessToken, String refreshToken) {
        // 将令牌加入黑名单
        userOperationDomain.logout(accessToken, refreshToken);
        // 清除SecurityContext
        SecurityContextHolder.clearContext();
    }
}
