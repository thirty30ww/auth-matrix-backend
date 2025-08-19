package com.thirty.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.AuthResultCode;
import com.thirty.user.model.dto.*;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.service.UserService;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    /**
     * 添加用户
     */
    @PostMapping("/add")
    public ResultDTO<Void> addUser(@RequestBody @Valid AddUserDTO addUserDTO, @RequestHeader(value = "Authorization") String authHeader) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);

        // 校验角色是否仅包含子角色
        if (!userService.validateRolesContainChildRoles(username, addUserDTO.getRoleIds())) {
            return ResultDTO.of(UserResultCode.ROLE_NOT_AUTHORIZED_ADD);
        }

        // 校验用户名是否存在
        if (userService.validateUserExists(addUserDTO.getUsername())) {
            return ResultDTO.of(AuthResultCode.USERNAME_EXISTS);
        }

        userService.addUser(addUserDTO);
        return ResultDTO.of(UserResultCode.USER_ADD_SUCCESS);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/get")
    public ResultDTO<UserVO> getUser(@RequestHeader(value = "Authorization") String authHeader) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);
        return ResultDTO.of(UserResultCode.USER_INFO_GET_SUCCESS, userService.getByUsername(username));
    }

    /**
     * 修改用户(指修改别的用户)
     */
    @PostMapping("/modify")
    public ResultDTO<Void> modifyUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid ModifyUserDTO modifyUserDTO) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);

        // 校验角色是否仅包含子角色
        if (!userService.validateRolesContainChildRoles(username, modifyUserDTO.getRoleIds())) {
            return ResultDTO.of(UserResultCode.ROLE_NOT_AUTHORIZED_MODIFY);
        }

        userService.modifyUser(modifyUserDTO);
        return ResultDTO.of(UserResultCode.USER_MODIFY_SUCCESS);
    }

    /**
     * 更新用户信息(修改自己的个人信息)
     */
    @PostMapping("/update")
    public ResultDTO<Void> updateUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid UpdateUserDTO request) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);
        userService.updateByUsername(username, request);
        return ResultDTO.of(UserResultCode.USER_INFO_UPDATE_SUCCESS);
    }

    /**
     * 封禁用户
     */
    @PostMapping("/ban")
    public ResultDTO<Void> banUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody List<Integer> userIds) {
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);
        // 校验角色是否仅包含子角色
        if (!userService.validateUserIdsRolesContainChildRoles(username, userIds)) {
            return ResultDTO.of(UserResultCode.ROLE_NOT_AUTHORIZED_BAN);
        }
        userService.banUser(userIds);
        return ResultDTO.of(UserResultCode.USER_BAN_SUCCESS);
    }

    /**
     * 解封用户
     */
    @PostMapping("/unban")
    public ResultDTO<Void> unbanUser(@RequestHeader(value = "Authorization") String authHeader, @RequestBody List<Integer> userIds) {
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);
        if (!userService.validateUserIdsRolesContainChildRoles(username, userIds)) {
            return ResultDTO.of(UserResultCode.ROLE_NOT_AUTHORIZED_UNBAN);
        }
        userService.unbanUser(userIds);
        return ResultDTO.of(UserResultCode.USER_UNBAN_SUCCESS);
    }

    /**
     * 修改密码
     * 需要用户先登录，通过JWT令牌获取当前用户身份
     */
    @PostMapping("/change-password")
    public ResultDTO<Void> changePassword(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);

        // 调用service修改密码
        userService.changePassword(username, changePasswordDTO);

        return ResultDTO.of(UserResultCode.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 获取用户列表
     * @param request 获取用户列表请求参数
     * @return 用户列表
     */
    @PostMapping("/list")
    public ResultDTO<IPage<UserVO>> getUserList(@RequestHeader(value = "Authorization") String authHeader, @RequestBody @Valid PageQueryDTO<GetUserListDTO> request) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);
        return ResultDTO.of(UserResultCode.USER_LIST_GET_SUCCESS, userService.getUserList(username, request));
    }

    /**
     * 获取角色列表
     * @param isChild 是否仅获取子角色列表
     * @return 角色列表
     */
    @GetMapping("/role/list")
    public ResultDTO<List<Role>> getRoleList(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(value = "isChild", defaultValue = "false") Boolean isChild) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);

        return ResultDTO.of(UserResultCode.ROLE_LIST_GET_SUCCESS, userService.getRoleList(username, isChild));
    }
}