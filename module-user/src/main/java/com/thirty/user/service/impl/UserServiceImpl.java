package com.thirty.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.api.SettingApi;
import com.thirty.common.exception.BusinessException;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.converter.UserDtoConverter;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.entity.Detail;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.service.DetailService;
import com.thirty.user.service.RoleService;
import com.thirty.user.service.UserRoleService;
import com.thirty.user.service.UserService;
import com.thirty.user.mapper.UserMapper;
import com.thirty.user.utils.UserUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
* @author Lenovo
* @description 针对表【user(用户主表)】的数据库操作Service实现
* @createDate 2025-07-07 15:24:51
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserUtil userUtil;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DetailService detailService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    @Resource
    private SettingApi settingApi;

    /**
     * 检查用户是否存在
     * @param username 用户名
     * @return 如果用户存在则返回true, 否则返回false
     */
    @Override
    public boolean validateUserExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper) != null;
    }

    /**
     * 校验角色是否包含子角色
     * @param username 用户名
     * @param roleIds 角色ID列表
     * @return 如果角色包含子角色则返回true, 否则返回false
     */
    @Override
    public boolean validateRolesContainChildRoles(String username, List<Integer> roleIds) {
        List<Role> roleList = roleService.getRoleList(username, true);

        // 获取当前用户有权限的角色ID列表
        List<Integer> permittedRoleIds = userUtil.getRoleIdsFromRoles(roleList);

        // 检查roleIds是否全都包含在roleList内
        for (Integer roleId : roleIds) {
            if (!permittedRoleIds.contains(roleId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 校验用户列表中每个用户是否有指定的权限
     * @param username 用户名
     * @param userIds 用户ID列表
     * @return 如果用户列表中每个用户都有指定的权限则返回true, 否则返回false
     */
    @Override
    public boolean validateUserIdsRolesContainChildRoles(String username, List<Integer> userIds) {
        List<Integer> roleIds = userRoleService.getRoleIdsByUserIds(userIds);
        return validateRolesContainChildRoles(username, roleIds);
    }

    /**
     * 添加用户
     * @param addUserDTO 添加用户请求参数
     */
    @Override
    public void addUser(AddUserDTO addUserDTO) {
        // 新建用户
        User user = new User();
        user.setUsername(addUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));
        save(user);

        // 提取新用户ID
        Integer newUserId = user.getId();

        // 关联用户信息
        Detail detail = UserDtoConverter.INSTANCE.addUserDTOToDetail(addUserDTO);
        detail.setId(newUserId);
        detailService.save(detail);

        // 关联用户角色
        userRoleService.addUserRoles(newUserId, addUserDTO.getRoleIds());
    }

    /**
     * 创建用户
     * @param registerDTO 注册请求参数
     */
    @Override
    public void createUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        save(user);
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserVO getByUsername(String username) {
        User user = userUtil.getUserByUsername(username);

        Detail detail = detailService.getById(user.getId());

        // 获取用户角色
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());

        // 将User和Detail对象转换为UserVO
        return UserDtoConverter.INSTANCE.toUserResponse(user, detail, roles);
    }

    /**
     * 修改用户信息
     * @param modifyUserDTO 修改用户请求参数
     */
    @Override
    public void modifyUser(ModifyUserDTO modifyUserDTO) {
        // 检查用户是否存在
        User user = getById(modifyUserDTO.getId());
        if (user == null) {
            throw new BusinessException(UserResultCode.USER_NOT_EXISTS);
        }

        // 将ModifyUserDTO转换为Detail对象
        Detail detail = UserDtoConverter.INSTANCE.modifyUserDTOToDetail(modifyUserDTO);

        // 更新Detail信息
        detailService.updateById(detail);

        // 更新用户角色
        userRoleService.deleteUserRoles(user.getId());
        userRoleService.addUserRoles(user.getId(), modifyUserDTO.getRoleIds());
    }

    /**
     * 根据用户名更新用户信息
     * @param username 用户名
     * @param request 更新的请求参数
     */
    @Override
    public void updateByUsername(String username, UpdateUserDTO request) {
        User user = userUtil.getUserByUsername(username);

        // 若用户ID不匹配, 抛出异常
        if (!Objects.equals(user.getId(), request.getId())) {
            throw new BusinessException(UserResultCode.USER_ID_MISMATCH);
        }

        // 将UpdateDTO转换为Detail对象
        Detail detail = UserDtoConverter.INSTANCE.updateUserDTOToDetail(request);

        // 更新Detail信息
        detailService.updateById(detail);
    }

    /**
     * 封禁用户
     * @param userIds 要封禁的用户ID列表
     */
    @Override
    public void banUser(List<Integer> userIds) {
        // 将用户状态设置为无效
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", userIds);
        updateWrapper.set("is_valid", false);
        update(updateWrapper);
    }

    /**
     * 解封用户
     * @param userIds 要解封的用户ID列表
     */
    @Override
    public void unbanUser(List<Integer> userIds) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", userIds);
        updateWrapper.set("is_valid", true);
        update(updateWrapper);
    }

    /**
     * 修改用户密码
     * @param username 用户名
     * @param request 修改密码请求参数
     */
    @Override
    public void changePassword(String username, ChangePasswordDTO request) {
        // 验证新密码与确认密码是否匹配
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(UserResultCode.PASSWORD_MISMATCH);
        }

        User user = userUtil.getUserByUsername(username);

        // 验证当前密码是否正确
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(UserResultCode.CURRENT_PASSWORD_INCORRECT);
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        updateById(user);
    }

    /**
     * 获取用户列表
     * @param request 获取用户列表请求参数
     * @return 用户列表
     */
    @Override
    public IPage<UserVO> getUserList(String username, PageQueryDTO<GetUserListDTO> request) {
        IPage<UserVO> page = new Page<>(request.getPageNum(), request.getPageSize());
        GetUserListDTO params = request.getParams();
        IPage<UserVO> userList = userMapper.getUserList(page, params);

        // 获取当前用户有权限的角色ID列表
        List<Role> roleList = roleService.getRoleList(username, true);
        List<Integer> permittedRoleIds = userUtil.getRoleIdsFromRoles(roleList);

        // 校验用户权限
        userUtil.setHasPermission(permittedRoleIds, userList);

        // 是否仅显示有权限操作的用户
        if (settingApi.isPermissionDisplay()) {
            // 过滤出有权限的用户
            userUtil.filterHasPermission(permittedRoleIds, userList);
        }
        
        return userList;
    }


}




