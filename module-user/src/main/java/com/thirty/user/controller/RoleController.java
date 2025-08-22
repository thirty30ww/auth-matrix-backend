package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.RoleResultCode;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.RoleService;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取角色树
     * @return 角色树
     */
    @GetMapping("/tree")
    public ResultDTO<List<RoleVO>> getRoleTree(@RequestHeader(value = "Authorization") String authHeader) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);

        return ResultDTO.of(RoleResultCode.ROLE_TREE_GET_SUCCESS, roleService.getRoleTree(username));
    }

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    @GetMapping("/global/list")
    public ResultDTO<List<Role>> getGlobalRoleList() {
        return ResultDTO.of(RoleResultCode.ROLE_LIST_GET_SUCCESS, roleService.getGlobalRoleList());
    }

    /**
     * 获取角色列表
     * @param isChild 是否仅获取子角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    public ResultDTO<List<Role>> getRoleList(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(value = "isChild", defaultValue = "false") Boolean isChild) {
        // 从Authorization头中提取用户名
        String username = jwtUtil.getUsernameFromAuthHeader(authHeader);

        return ResultDTO.of(RoleResultCode.ROLE_LIST_GET_SUCCESS, roleService.getRoleList(username, isChild));
    }
}
