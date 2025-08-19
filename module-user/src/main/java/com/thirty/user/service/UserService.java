package com.thirty.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【user(用户主表)】的数据库操作Service
* @createDate 2025-07-07 15:24:51
*/
public interface UserService extends IService<User> {
    /**
     * 校验用户列表中每个用户是否有指定的权限
     * @param username 用户名
     * @param userIds 用户ID列表
     * @return 如果用户列表中每个用户都有指定的权限则返回true, 否则返回false
     */
    boolean validateUserIdsRolesContainChildRoles(String username, List<Integer> userIds);

    /**
     * 添加用户
     * @param addUserDTO 添加用户请求参数
     */
    void addUser(AddUserDTO addUserDTO);

    /**
     * 校验用户名是否存在
     * @param username 用户名
     * @return true 存在 false 不存在
     */
    boolean validateUserExists(String username);

    /**
     * 校验角色是否包含子角色
     * @param username 用户名
     * @param roleIds 角色ID列表
     * @return true 包含 false 不包含
     */
    boolean validateRolesContainChildRoles(String username, List<Integer> roleIds);

    /**
     * 注册用户
     * @param request 注册请求参数
     */
    void createUser(RegisterDTO request);

    /**
     * 根据用户名获取用户信息
     */
    UserVO getByUsername(String username);

    /**
     * 修改用户信息
     * @param modifyUserDTO 修改用户请求参数
     */
    void modifyUser(ModifyUserDTO modifyUserDTO);

    /**
     * 根据用户名更新用户信息
     */
    void updateByUsername(String username, UpdateUserDTO request);

    /**
     * 封禁用户
     * @param userIds 要封禁的用户ID列表
     */
    void banUser(List<Integer> userIds);

    /**
     * 解封用户
     * @param userIds 要解封的用户ID列表
     */
    void unbanUser(List<Integer> userIds);

    /**
     * 修改用户密码
     */
    void changePassword(String username, ChangePasswordDTO request);

    /**
     * 获取用户列表
     * @param request 获取用户列表请求参数
     * @return 用户列表
     */
    IPage<UserVO> getUserList(String username, PageQueryDTO<GetUserListDTO> request);

    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Role> getRoleList(String username, Boolean isChild);
}
