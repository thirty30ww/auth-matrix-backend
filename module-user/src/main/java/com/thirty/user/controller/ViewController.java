package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.ViewResultCode;
import com.thirty.user.model.dto.ViewDTO;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.facade.ViewFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面管理
 */
@RestController
@RequestMapping("/view")
public class ViewController {
    @Resource
    private ViewFacade viewFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取视图树
     * @return 视图树
     */
    @GetMapping("/tree")
    public ResultDTO<List<ViewVO>> getViewTree(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<ViewVO> viewTree = viewFacade.getViewTree(userId);
        return ResultDTO.of(ViewResultCode.GET_VIEW_TREE_SUCCESS, viewTree);
    }

    /**
     * 获取菜单树
     * @param authHeader 授权头
     * @return 菜单树
     */
    @GetMapping("/menu/tree")
    public ResultDTO<List<ViewVO>> getMenuTree(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<ViewVO> menuTree = viewFacade.getMenuTree(userId);
        return ResultDTO.of(ViewResultCode.GET_MENU_TREE_SUCCESS, menuTree);
    }

    /**
     * 获取目录树
     * @param authHeader 授权头
     * @return 目录树
     */
    @GetMapping("/directory/tree")
    public ResultDTO<List<ViewVO>> getDirectoryTree(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<ViewVO> directoryTree = viewFacade.getDirectoryTree(userId);
        return ResultDTO.of(ViewResultCode.GET_DIRECTORY_TREE_SUCCESS, directoryTree);
    }

    /**
     * 获取菜单和按钮树
     * @param targetRoleId 目标角色ID
     * @param authHeader 授权头
     * @return 菜单和按钮树
     */
    @GetMapping("/menu/button/tree")
    public ResultDTO<List<ViewVO>> getMenuAndButtonTree(@RequestParam(required = false) Integer targetRoleId, @RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<ViewVO> menuAndButtonTree = viewFacade.getMenuAndButtonTree(userId, targetRoleId);
        return ResultDTO.of(ViewResultCode.GET_MENU_AND_BUTTON_TREE_SUCCESS, menuAndButtonTree);
    }

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    @GetMapping("/list")
    public ResultDTO<List<View>> getViewList(@RequestParam(required = false) String keyword) {
        List<View> viewList = viewFacade.getViews(keyword);
        return ResultDTO.of(ViewResultCode.GET_LIST_SUCCESS, viewList);
    }

    /**
     * 获取权限码列表
     * @return 权限码列表
     */
    @GetMapping("/permission/code")
    public ResultDTO<List<String>> getPermissionCode(@RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<String> permissionCodeList = viewFacade.getPermissionCode(userId);
        return ResultDTO.of(ViewResultCode.GET_PERMISSION_CODE_SUCCESS, permissionCodeList);
    }

    /**
     * 添加视图
     * @param authHeader 授权头
     * @param viewDTO 视图DTO
     * @return 结果
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('permission:menu:add')")
    public ResultDTO<Void> addView(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid ViewDTO viewDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        viewFacade.addView(userId, viewDTO);
        return ResultDTO.of(ViewResultCode.ADD_SUCCESS);
    }

    /**
     * 修改视图
     * @param authHeader 授权头
     * @param viewDTO 视图DTO
     * @return 结果
     */
    @PostMapping("/modify")
    @PreAuthorize("hasAuthority('permission:menu:modify')")
    public ResultDTO<Void> modifyView(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid ViewDTO viewDTO) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        viewFacade.modifyView(userId, viewDTO);
        return ResultDTO.of(ViewResultCode.MODIFY_SUCCESS);
    }

    /**
     * 删除视图
     * @param authHeader 授权头
     * @param viewId 视图ID
     * @return 结果
     */
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('permission:menu:delete')")
    public ResultDTO<Void> deleteView(@RequestHeader("Authorization") String authHeader, @RequestParam Integer viewId) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        viewFacade.deleteView(userId, viewId);
        return ResultDTO.of(ViewResultCode.DELETE_SUCCESS);
    }

    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     * @return 结果
     */
    @GetMapping("/move")
    @PreAuthorize("hasAuthority('permission:menu:move')")
    public ResultDTO<Void> moveView(@RequestParam Integer viewId, @RequestParam Boolean isUp) {
        viewFacade.moveView(viewId, isUp);
        return ResultDTO.of(ViewResultCode.MOVE_SUCCESS);
    }

}
