package com.thirty.user.controller;

import com.thirty.common.annotation.OperateLog;
import com.thirty.common.annotation.OperateModule;
import com.thirty.common.enums.model.OperationType;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.PermissionResultCode;
import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.vo.PermissionVO;
import com.thirty.user.service.facade.PermissionFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面管理
 */
@RestController
@RequestMapping("/permission")
@OperateModule("权限管理")
public class PermissionController {
    @Resource
    private PermissionFacade permissionFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取页面树
     * @return 页面树
     */
    @GetMapping("/tree")
    @OperateLog(type = OperationType.SELECT, description = "获取页面树")
    public ResultDTO<List<PermissionVO>> getViewTree(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<PermissionVO> viewTree = permissionFacade.getViewTree(userId);
        return ResultDTO.of(PermissionResultCode.GET_VIEW_TREE_SUCCESS, viewTree);
    }

    /**
     * 获取菜单树
     * @param authHeader 授权头
     * @return 菜单树
     */
    @GetMapping("/menu/tree")
    @OperateLog(type = OperationType.SELECT, description = "获取菜单树")
    public ResultDTO<List<PermissionVO>> getMenuTree(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<PermissionVO> menuTree = permissionFacade.getMenuTree(userId);
        return ResultDTO.of(PermissionResultCode.GET_MENU_TREE_SUCCESS, menuTree);
    }

    /**
     * 获取目录树
     * @param authHeader 授权头
     * @return 目录树
     */
    @GetMapping("/directory/tree")
    @OperateLog(type = OperationType.SELECT, description = "获取目录树")
    public ResultDTO<List<PermissionVO>> getDirectoryTree(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<PermissionVO> directoryTree = permissionFacade.getDirectoryTree(userId);
        return ResultDTO.of(PermissionResultCode.GET_DIRECTORY_TREE_SUCCESS, directoryTree);
    }

    /**
     * 获取菜单和按钮树
     * @param targetRoleId 目标角色ID
     * @param authHeader 授权头
     * @return 菜单和按钮树
     */
    @GetMapping("/menu/button/tree")
    @OperateLog(type = OperationType.SELECT, description = "获取菜单和按钮树")
    public ResultDTO<List<PermissionVO>> getMenuAndButtonTree(@RequestParam(required = false) Integer targetRoleId, @RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<PermissionVO> menuAndButtonTree = permissionFacade.getMenuAndButtonTree(userId, targetRoleId);
        return ResultDTO.of(PermissionResultCode.GET_MENU_AND_BUTTON_TREE_SUCCESS, menuAndButtonTree);
    }

    /**
     * 获取权限列表
     * @param keyword 权限名称关键词
     * @return 权限列表
     */
    @GetMapping("/list")
    @OperateLog(type = OperationType.SELECT, description = "获取权限列表")
    public ResultDTO<List<PermissionVO>> getViewList(@RequestParam(required = false) String keyword, @RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<PermissionVO> viewList = permissionFacade.getViewVOS(userId, keyword);
        return ResultDTO.of(PermissionResultCode.GET_LIST_SUCCESS, viewList);
    }

    /**
     * 获取权限码列表
     * @return 权限码列表
     */
    @GetMapping("/permission/code")
    @OperateLog(type = OperationType.SELECT, description = "获取权限码列表")
    public ResultDTO<List<String>> getPermissionCode(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<String> permissionCodeList = permissionFacade.getPermissionCode(userId);
        return ResultDTO.of(PermissionResultCode.GET_PERMISSION_CODE_SUCCESS, permissionCodeList);
    }

    /**
     * 添加权限
     * @param authHeader 授权头
     * @param permissionDTO 权限DTO
     * @return 结果
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('permission:menu:add')")
    @OperateLog(type = OperationType.INSERT, description = "添加权限")
    public ResultDTO<Void> addPermission(@RequestHeader("Authorization") String authHeader, @RequestBody @Validated(PermissionDTO.Add.class) PermissionDTO permissionDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        permissionFacade.addPermission(userId, permissionDTO);
        return ResultDTO.of(PermissionResultCode.ADD_SUCCESS);
    }

    /**
     * 修改权限
     * @param authHeader 授权头
     * @param permissionDTO 权限DTO
     * @return 结果
     */
    @PostMapping("/modify")
    @PreAuthorize("hasAuthority('permission:menu:modify')")
    @OperateLog(type = OperationType.UPDATE, description = "修改权限")
    public ResultDTO<Void> modifyPermission(@RequestHeader("Authorization") String authHeader, @RequestBody @Validated(PermissionDTO.Modify.class) PermissionDTO permissionDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        permissionFacade.modifyPermission(userId, permissionDTO);
        return ResultDTO.of(PermissionResultCode.MODIFY_SUCCESS);
    }

    /**
     * 删除权限
     * @param authHeader 授权头
     * @param permissionId 权限ID
     * @return 结果
     */
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('permission:menu:delete')")
    @OperateLog(type = OperationType.DELETE, description = "删除权限")
    public ResultDTO<Void> deletePermission(@RequestHeader("Authorization") String authHeader, @RequestParam Integer permissionId) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        permissionFacade.deletePermission(userId, permissionId);
        return ResultDTO.of(PermissionResultCode.DELETE_SUCCESS);
    }

    /**
     * 移动权限
     * @param permissionId 权限ID
     * @param isUp 是否上移
     * @return 结果
     */
    @GetMapping("/move")
    @PreAuthorize("hasAuthority('permission:menu:move')")
    @OperateLog(type = OperationType.UPDATE, description = "移动权限")
    public ResultDTO<Void> movePermission(@RequestParam Integer permissionId, @RequestParam Boolean isUp) {
        permissionFacade.movePermission(permissionId, isUp);
        return ResultDTO.of(PermissionResultCode.MOVE_SUCCESS);
    }

}
