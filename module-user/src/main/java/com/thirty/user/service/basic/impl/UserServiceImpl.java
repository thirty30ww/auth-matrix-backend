package com.thirty.user.service.basic.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.mapper.UserMapper;
import com.thirty.user.model.dto.GetUsersDTO;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.basic.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户基础服务实现 - 职责单一，无复杂依赖
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Resource
    private UserMapper userMapper;

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
     * 检查用户是否存在
     * @param userId 用户ID
     * @return 如果用户存在则返回true, 否则返回false
     */
    @Override
    public boolean validateUserExists(Integer userId) {
        return getById(userId) != null;
    }

    /**
     * 创建用户
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public Integer createUser(String username, String password) {
        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        save(user);

        // 返回用户ID
        return user.getId();
    }

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    /**
     * 封禁用户
     * @param userIds 要封禁的用户ID列表
     */
    @Override
    public void banUsers(List<Integer> userIds) {
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
    public void unbanUsers(List<Integer> userIds) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", userIds);
        updateWrapper.set("is_valid", true);
        update(updateWrapper);
    }

    /**
     * 获取用户列表
     * @param request 获取用户列表请求参数
     * @return 用户列表
     */
    @Override
    public IPage<UserVO> getUsers(PageQueryDTO<GetUsersDTO> request) {
        IPage<UserVO> page = new Page<>(request.getPageNum(), request.getPageSize());
        return userMapper.getUsers(page, request.getParams());
    }
}