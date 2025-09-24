package com.thirty.user.controller;

import com.thirty.common.annotation.OperateLog;
import com.thirty.common.annotation.OperateModule;
import com.thirty.common.enums.model.OperationType;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.model.RolesType;
import com.thirty.user.enums.result.RoleResultCode;
import com.thirty.user.model.dto.AssignPermissionDTO;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.facade.RoleFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
@OperateModule("角色管理")
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
    @OperateLog(type = OperationType.SELECT, description = "获取角色树")
    public ResultDTO<List<RoleVO>> getRoleTree(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(value = "type", defaultValue = "ALL") RolesType type) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(RoleResultCode.ROLE_TREE_GET_SUCCESS, roleFacade.getRoleTree(userId, type));
    }

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    @OperateLog(type = OperationType.SELECT, description = "获取角色列表")
    public ResultDTO<List<Role>> getRoleList(@RequestHeader(value = "Authorization") String authHeader, @RequestParam(value = "type", defaultValue = "ALL") RolesType type) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(RoleResultCode.ROLE_LIST_GET_SUCCESS, roleFacade.getRoles(userId, type));
    }

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('permission:role:add')")
    @OperateLog(type = OperationType.INSERT, description = "添加角色")
    public ResultDTO<Void> addRole(@Validated(RoleDTO.Add.class) @RequestBody RoleDTO roleDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.addRole(roleDTO, userId);
        return ResultDTO.of(RoleResultCode.ROLE_ADD_SUCCESS);
    }

    /**
     * 添加全局角色
     * @param roleDTO 角色dto
     */
    @PostMapping("/add/global")
    @PreAuthorize("hasAuthority('permission:role:global') and hasAuthority('permission:role:add')")
    @OperateLog(type = OperationType.INSERT, description = "添加全局角色")
    public ResultDTO<Void> addGlobalRole(@Validated(RoleDTO.GlobalAdd.class) @RequestBody RoleDTO roleDTO) {
        roleFacade.addGlobalRole(roleDTO);
        return ResultDTO.of(RoleResultCode.ROLE_ADD_SUCCESS);
    }

    /**
     * 更新角色
     * @param roleDTO 角色dto
     */
    @PostMapping("/modify")
    @PreAuthorize("hasAuthority('permission:role:modify')")
    @OperateLog(type = OperationType.UPDATE, description = "修改角色")
    public ResultDTO<Void> updateRole(@Validated(RoleDTO.Modify.class) @RequestBody RoleDTO roleDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.updateRole(roleDTO, userId);
        return ResultDTO.of(RoleResultCode.ROLE_UPDATE_SUCCESS);
    }

    /**
     * 更新全局角色
     * @param roleDTO 角色dto
     */ 
    @PostMapping("/modify/global")
    @PreAuthorize("hasAuthority('permission:role:global') and hasAuthority('permission:role:modify')")
    @OperateLog(type = OperationType.UPDATE, description = "修改全局角色")
    public ResultDTO<Void> updateGlobalRole(@Validated(RoleDTO.GlobalModify.class) @RequestBody RoleDTO roleDTO) {
        roleFacade.updateGlobalRole(roleDTO);
        return ResultDTO.of(RoleResultCode.ROLE_UPDATE_SUCCESS);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     */
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('permission:role:delete')")
    @OperateLog(type = OperationType.DELETE, description = "删除角色")
    public ResultDTO<Void> deleteRole(@RequestParam(value = "roleId") Integer roleId, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.deleteRole(roleId, userId);
        return ResultDTO.of(RoleResultCode.ROLE_DELETE_SUCCESS);
    }

    /**
     * 删除全局角色
     * @param roleId 角色ID
     */
    @GetMapping("/delete/global")
    @PreAuthorize("hasAuthority('permission:role:global') and hasAuthority('permission:role:delete')")
    @OperateLog(type = OperationType.DELETE, description = "删除全局角色")
    public ResultDTO<Void> deleteGlobalRole(@RequestParam(value = "roleId") Integer roleId) {
        roleFacade.deleteGlobalRole(roleId);
        return ResultDTO.of(RoleResultCode.ROLE_DELETE_SUCCESS);
    }

    /**
     * 分配权限权限
     */
    @PostMapping("/assign/permission")
    @PreAuthorize("hasAuthority('permission:role:assign')")
    @OperateLog(type = OperationType.UPDATE, description = "分配角色权限")
    public ResultDTO<Void> assignPermission(@RequestBody AssignPermissionDTO assignPermissionDTO, @RequestHeader(value = "Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        roleFacade.assignPermission(userId, assignPermissionDTO);
        return ResultDTO.of(RoleResultCode.ROLE_ASSIGN_VIEW_SUCCESS);
    }

    /**
     * 分配全局权限权限
     */
    @PostMapping("/assign/permission/global")
    @PreAuthorize("hasAuthority('permission:role:global') and hasAuthority('permission:role:assign')")
    @OperateLog(type = OperationType.UPDATE, description = "分配全局角色权限")
    public ResultDTO<Void> assignGlobalPermission(@RequestBody AssignPermissionDTO assignPermissionDTO) {
        roleFacade.assignGlobalPermission(assignPermissionDTO);
        return ResultDTO.of(RoleResultCode.ROLE_ASSIGN_VIEW_SUCCESS);
    }
}
