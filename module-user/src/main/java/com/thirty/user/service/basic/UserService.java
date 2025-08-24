package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.GetUsersDTO;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.UserVO;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 校验用户名是否存在
     * @param username 用户名
     * @return true 存在 false 不存在
     */
    boolean validateUserExists(String username);

    /**
     * 创建用户
     * @param username 用户名
     * @param encodedPassword 加密后的密码
     * @return 用户ID
     */
    Integer createUser(String username, String encodedPassword);

    /**
     * 获取用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUser(String username);

    /**
     * 封禁用户
     * @param userIds 要封禁的用户ID列表
     */
    void banUsers(List<Integer> userIds);

    /**
     * 解封用户
     * @param userIds 要解封的用户ID列表
     */
    void unbanUsers(List<Integer> userIds);

    /**
     * 获取用户列表
     * @param request 获取用户列表请求参数
     * @return 用户列表
     */
    IPage<UserVO> getUsers(PageQueryDTO<GetUsersDTO> request);
}
