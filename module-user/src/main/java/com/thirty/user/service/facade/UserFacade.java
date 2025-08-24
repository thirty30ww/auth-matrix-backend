package com.thirty.user.service.facade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.vo.UserVO;

import java.util.List;

public interface UserFacade {
    /**
     * 添加用户
     * @param operatorUsername 操作人用户名
     * @param addUserDTO 添加用户DTO
     */
    void addUser(String operatorUsername, AddUserDTO addUserDTO);

    /**
     * 获取用户
     * @param currentUsername 当前用户名
     * @return 用户VO
     */
    UserVO getUser(String currentUsername);

    /**
     * 修改用户
     * @param operatorUsername 操作人用户名
     * @param modifyUserDTO 修改用户DTO
     */
    void modifyUser(String operatorUsername, ModifyUserDTO modifyUserDTO);

    /**
     * 更新用户
     * @param currentUsername 当前用户名
     * @param updateUserDTO 更新用户DTO
     */
    void updateUser(String currentUsername, UpdateUserDTO updateUserDTO);

    /**
     * 封禁用户
     * @param operatorUsername 操作人用户名
     * @param userIds 用户ID列表
     */
    void banUsers(String operatorUsername, List<Integer> userIds);

    /**
     * 解封用户
     * @param operatorUsername 操作人用户名
     * @param userIds 用户ID列表
     */
    void unbanUsers(String operatorUsername, List<Integer> userIds);

    /**
     * 改变密码
     * @param username 用户名
     * @param changePasswordDTO 改变密码DTO
     */
    void changePassword(String username, ChangePasswordDTO changePasswordDTO);

    /**
     * 获取用户列表
     * @param currentUsername 当前用户名
     * @param pageQueryDTO 分页查询DTO
     * @return 用户VO分页
     */
    IPage<UserVO> getUsers(String currentUsername, PageQueryDTO<GetUsersDTO> pageQueryDTO);

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    void logout(String accessToken, String refreshToken);
}
