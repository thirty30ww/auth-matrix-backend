package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.model.RoleListType;
import com.thirty.user.enums.result.RoleResultCode;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.facade.RoleFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleFacade roleFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取角色树
     * @return 角色树
     */
    @GetMapping("/tree")
    public ResultDTO<List<RoleVO>> getRoleTree(@RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(RoleResultCode.ROLE_TREE_GET_SUCCESS, roleFacade.getRoleTree(userId));
    }

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    public ResultDTO<List<Role>> getRoleList(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(value = "type", defaultValue = "ALL") RoleListType type) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(RoleResultCode.ROLE_LIST_GET_SUCCESS, roleFacade.getRoles(userId, type));
    }

    /**
     * 添加角色
     * @param roleDTO 角色dto
     * @return 角色dto
     */
    @PostMapping("/add")
    public ResultDTO<Void> addRole(@Validated(RoleDTO.Add.class) @RequestBody RoleDTO roleDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.addRole(roleDTO, userId);
        return ResultDTO.of(RoleResultCode.ROLE_ADD_SUCCESS);
    }

    /**
     * 更新角色
     * @param roleDTO 角色dto
     * @return 角色dto
     */
    @PostMapping("/update")
    public ResultDTO<Void> updateRole(@Validated(RoleDTO.Update.class) @RequestBody RoleDTO roleDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.updateRole(roleDTO, userId);
        return ResultDTO.of(RoleResultCode.ROLE_UPDATE_SUCCESS);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     * @return 角色dto
     */
    @GetMapping("/delete")
    public ResultDTO<Void> deleteRole(@RequestParam(value = "roleId") Integer roleId, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.deleteRole(roleId, userId);
        return ResultDTO.of(RoleResultCode.ROLE_DELETE_SUCCESS);
    }
}
