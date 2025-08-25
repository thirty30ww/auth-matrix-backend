package com.thirty.user.service.domain.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.exception.BusinessException;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.converter.UserDtoConverter;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.dto.GetUsersDTO;
import com.thirty.user.model.entity.Detail;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.basic.DetailService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.UserQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserQueryDomainImpl implements UserQueryDomain {

    @Resource
    private UserService userService;
    @Resource
    private DetailService detailService;
    @Resource
    private UserRoleService userRoleService;


    /**
     * 获取用户
     * @param currentUsername 当前用户名
     * @return 用户VO
     */
    @Override
    public UserVO getUser(String currentUsername) {
        // 获取用户
        User user = userService.getUser(currentUsername);

        // 获取用户详情
        Detail detail = detailService.getById(user.getId());

        // 获取用户角色
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());

        return UserDtoConverter.INSTANCE.toUserResponse(user, detail, roles);
    }

    /**
     * 获取用户
     * @param userId 用户ID
     * @return 用户VO
     */
    @Override
    public UserVO getUser(Integer userId) {
        // 获取用户
        User user = userService.getById(userId);

        // 校验用户是否存在
        if (!userService.validateUserExists(userId)) {
            throw new BusinessException(UserResultCode.USER_NOT_EXISTS);
        }

        // 获取用户详情
        Detail detail = detailService.getById(user.getId());

        // 获取用户角色
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());

        return UserDtoConverter.INSTANCE.toUserResponse(user, detail, roles);
    }

    /**
     * 获取用户列表
     * @param pageQueryDTO 获取用户列表请求参数
     * @param permittedRoleIds 有权限的角色ID列表
     * @param hasPermissionDisplay 是否仅显示有权限操作的用户
     * @return 用户列表
     */
    @Override
    public IPage<UserVO> getUsers(PageQueryDTO<GetUsersDTO> pageQueryDTO, List<Integer> permittedRoleIds, boolean hasPermissionDisplay) {
        // 获取基础用户列表
        IPage<UserVO> users = userService.getUsers(pageQueryDTO);

        // 设置用户列表中每个用户的hasPermission属性
        setUsersHasPermission(permittedRoleIds, users);

        // 是否仅显示有权限操作的用户
        if (hasPermissionDisplay) {
            // 过滤出有权限操作的用户
            filterUsersHasPermission(permittedRoleIds, users);
        }

        return users;
    }

    /**
     * 验证用户列表中每个用户是否有指定的权限
     * @param permittedRoleIds 权限角色ID列表
     * @param users 用户列表
     */
    private void setUsersHasPermission(List<Integer> permittedRoleIds, IPage<UserVO> users) {
        // 遍历每个UserVO，检查权限
        for (UserVO userVO : users.getRecords()) {
            // 获取列表用户的角色
            List<Role> roles = userVO.getRoles();

            // 获取列表用户的角色ID
            List<Integer> userRoleIds = Role.extractIds(roles);

            // 如果用户的所有角色ID都包含在权限角色列表中，则hasPermission为true
            boolean hasPermission = new HashSet<>(permittedRoleIds).containsAll(userRoleIds);
            userVO.setHasPermission(hasPermission);
        }
    }

    /**
     * 过滤出用户列表中有权限的用户
     * @param permittedRoleIds 权限角色ID列表
     * @param users 用户列表
     */
    private void filterUsersHasPermission(List<Integer> permittedRoleIds, IPage<UserVO> users) {
        users.getRecords().removeIf(userVO -> !userVO.getHasPermission());
    }
}
