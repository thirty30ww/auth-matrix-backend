package com.thirty.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.facade.UserFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserFacade userFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 添加用户
     */
    @PostMapping("/add")
    public ResultDTO<Void> addUser(@RequestBody @Valid AddUserDTO addUserDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.addUser(userId, addUserDTO);
        return ResultDTO.of(UserResultCode.USER_ADD_SUCCESS);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/get")
    public ResultDTO<UserVO> getUser(@RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(UserResultCode.USER_INFO_GET_SUCCESS, userFacade.getUser(userId));
    }

    /**
     * 获取用户列表
     * @param request 获取用户列表请求参数
     * @return 用户列表
     */
    @PostMapping("/list")
    public ResultDTO<IPage<UserVO>> getUserList(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid PageQueryDTO<GetUsersDTO> request) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(UserResultCode.USER_LIST_GET_SUCCESS, userFacade.getUsers(userId, request));
    }

    /**
     * 修改用户(指修改别的用户)
     */
    @PostMapping("/modify")
    public ResultDTO<Void> modifyUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid ModifyUserDTO modifyUserDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.modifyUser(userId, modifyUserDTO);
        return ResultDTO.of(UserResultCode.USER_MODIFY_SUCCESS);
    }

    /**
     * 更新用户信息(修改自己的个人信息)
     */
    @PostMapping("/update")
    public ResultDTO<Void> updateUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid UpdateUserDTO request) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.updateUser(userId, request);
        return ResultDTO.of(UserResultCode.USER_INFO_UPDATE_SUCCESS);
    }

    /**
     * 封禁用户
     */
    @PostMapping("/ban")
    public ResultDTO<Void> banUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody List<Integer> userIds) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.banUsers(userId, userIds);
        return ResultDTO.of(UserResultCode.USER_BAN_SUCCESS);
    }

    /**
     * 解封用户
     */
    @PostMapping("/unban")
    public ResultDTO<Void> unbanUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody List<Integer> userIds) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.unbanUsers(userId, userIds);
        return ResultDTO.of(UserResultCode.USER_UNBAN_SUCCESS);
    }

    /**
     * 修改密码
     * 需要用户先登录，通过JWT令牌获取当前用户身份
     */
    @PostMapping("/change-password")
    public ResultDTO<Void> changePassword(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.changePassword(userId, changePasswordDTO);
        return ResultDTO.of(UserResultCode.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 退出登录
     * 将当前访问令牌和对应的刷新令牌都加入Redis黑名单，使其立即失效
     * 同时清除SecurityContext
     */
    @GetMapping("/logout")
    public ResultDTO<Void> logout(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(required = false) String refreshToken) {
        String accessToken = jwtUtil.extractToken(authHeader);
        userFacade.logout(accessToken, refreshToken);
        return ResultDTO.of(AuthResultCode.LOGOUT_SUCCESS);
    }
}