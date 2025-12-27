package com.thirty.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.annotation.OperateLog;
import com.thirty.common.annotation.OperateModule;
import com.thirty.common.annotation.RateLimiter;
import com.thirty.common.enums.model.LimitType;
import com.thirty.common.enums.model.OperationType;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.entity.Preference;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.facade.UserFacade;
import com.thirty.common.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@OperateModule("用户管理")
@RequestMapping("/user")
@RateLimiter(limitType = LimitType.TOKEN)
public class UserController {
    @Resource
    private UserFacade userFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 添加用户
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user:add')")
    @OperateLog(type = OperationType.INSERT, description = "添加用户")
    public ResultDTO<Void> addUser(@RequestBody @Valid AddUserDTO addUserDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.addUser(userId, addUserDTO);
        return ResultDTO.of(UserResultCode.USER_ADD_SUCCESS);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/get")
    @OperateLog(type = OperationType.SELECT, description = "获取用户信息")
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
    @OperateLog(type = OperationType.SELECT, description = "获取用户列表")
    public ResultDTO<IPage<UserVO>> getUserList(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid PageQueryDTO<GetUsersDTO> request) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(UserResultCode.USER_LIST_GET_SUCCESS, userFacade.getUsers(userId, request));
    }

    /**
     * 修改用户(指修改别的用户)
     */
    @PostMapping("/modify")
    @PreAuthorize("hasAuthority('user:modify')")
    @OperateLog(type = OperationType.UPDATE, description = "修改用户")
    public ResultDTO<Void> modifyUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid ModifyUserDTO modifyUserDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.modifyUser(userId, modifyUserDTO);
        return ResultDTO.of(UserResultCode.USER_MODIFY_SUCCESS);
    }

    /**
     * 更新用户信息(修改自己的个人信息)
     */
    @PostMapping("/update")
    @OperateLog(type = OperationType.UPDATE, description = "更新用户信息")
    public ResultDTO<Void> updateUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid UpdateUserDTO request) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.updateUser(userId, request);
        return ResultDTO.of(UserResultCode.USER_INFO_UPDATE_SUCCESS);
    }

    /**
     * 封禁用户
     */
    @PostMapping("/ban")
    @PreAuthorize("hasAuthority('user:ban')")
    @OperateLog(type = OperationType.UPDATE, description = "封禁用户")
    public ResultDTO<Void> banUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody List<Integer> userIds) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.banUsers(userId, userIds);
        return ResultDTO.of(UserResultCode.USER_BAN_SUCCESS);
    }

    /**
     * 解封用户
     */
    @PostMapping("/unban")
    @PreAuthorize("hasAuthority('user:unban')")
    @OperateLog(type = OperationType.UPDATE, description = "解封用户")
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
    @OperateLog(type = OperationType.UPDATE, description = "修改密码")
    public ResultDTO<Void> changePassword(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.changePassword(userId, changePasswordDTO);
        return ResultDTO.of(UserResultCode.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 获取用户偏好设置
     * @return 用户偏好设置列表
     */
    @GetMapping("/preferences/get")
    @OperateLog(type = OperationType.SELECT, description = "获取用户偏好设置")
    public ResultDTO<List<Preference>> getPreferences(@RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(UserResultCode.PREFERENCE_GET_SUCCESS, userFacade.getPreferences(userId));
    }

    /**
     * 保存用户偏好设置
     * @param field 偏好字段名
     * @param value 偏好值
     */
    @GetMapping("/preferences/save")
    @OperateLog(type = OperationType.UPDATE, description = "保存用户偏好设置")
    public ResultDTO<Void> savePreference(@RequestHeader(value = "Authorization") String authHeader, @RequestParam String field, @RequestParam String value) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        userFacade.savePreference(userId, field, value);
        return ResultDTO.of(UserResultCode.PREFERENCE_SAVE_SUCCESS);
    }
}