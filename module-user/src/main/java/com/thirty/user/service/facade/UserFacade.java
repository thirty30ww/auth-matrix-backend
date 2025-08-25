package com.thirty.user.service.facade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.vo.UserVO;

import java.util.List;

public interface UserFacade {
    /**
     * 添加用户
     * @param operatorUserId 操作人用户ID
     * @param addUserDTO 添加用户DTO
     */
    void addUser(Integer operatorUserId, AddUserDTO addUserDTO);

    /**
     * 获取用户
     * @param userId 用户ID
     * @return 用户VO
     */
    UserVO getUser(Integer userId);

    /**
     * 修改用户
     * @param operatorUserId 操作人用户ID
     * @param modifyUserDTO 修改用户DTO
     */
    void modifyUser(Integer operatorUserId, ModifyUserDTO modifyUserDTO);

    /**
     * 更新用户
     * @param operatorUserId 操作人用户ID
     * @param updateUserDTO 更新用户DTO
     */
    void updateUser(Integer operatorUserId, UpdateUserDTO updateUserDTO);

    /**
     * 封禁用户
     * @param operatorUserId 操作人用户ID
     * @param userIds 用户ID列表
     */
    void banUsers(Integer operatorUserId, List<Integer> userIds);

    /**
     * 解封用户
     * @param operatorUserId 操作人用户ID
     * @param userIds 用户ID列表
     */
    void unbanUsers(Integer operatorUserId, List<Integer> userIds);

    /**
     * 改变密码
     * @param userId 用户ID
     * @param changePasswordDTO 改变密码DTO
     */
    void changePassword(Integer userId, ChangePasswordDTO changePasswordDTO);

    /**
     * 获取用户列表
     * @param currentUserId 当前用户ID
     * @param pageQueryDTO 分页查询DTO
     * @return 用户VO分页
     */
    IPage<UserVO> getUsers(Integer currentUserId, PageQueryDTO<GetUsersDTO> pageQueryDTO);

    /**
     * 退出登录
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     */
    void logout(String accessToken, String refreshToken);
}
