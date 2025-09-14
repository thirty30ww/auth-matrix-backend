package com.thirty.user.service.facade.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.user.constant.ViewConstant;
import com.thirty.user.enums.model.ViewsType;
import com.thirty.user.enums.result.ViewResultCode;
import com.thirty.user.model.dto.ViewDTO;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.role.RoleQueryDomain;
import com.thirty.user.service.domain.role.builder.RolesBuilderFactory;
import com.thirty.user.service.domain.view.ViewOperationDomain;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import com.thirty.user.service.domain.view.ViewValidationDomain;
import com.thirty.user.service.domain.view.builder.ViewsBuilderFactory;
import com.thirty.user.service.facade.ViewFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ViewFacadeImpl implements ViewFacade {
    @Resource
    private ViewService viewService;

    @Resource
    private ViewQueryDomain viewQueryDomain;
    @Resource
    private ViewValidationDomain viewValidationDomain;
    @Resource
    private ViewOperationDomain viewOperationDomain;
    @Resource
    private RoleQueryDomain roleQueryDomain;

    @Resource
    private RolesBuilderFactory rolesBuilderFactory;
    @Resource
    private ViewsBuilderFactory viewsBuilderFactory;

    /**
     * 获取视图树
     * @param userId 用户ID
     * @return 视图树
     */
    @Override
    public List<ViewVO> getViewTree(Integer userId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return viewsBuilderFactory.create(currentRoleIds)
                .ofType(ViewsType.VIEW)
                .withPermissionFlag()
                .buildTree();
    }

    /**
     * 获取菜单树
     * @param userId 用户ID
     * @return 菜单树
     */
    @Override
    public List<ViewVO> getMenuTree(Integer userId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return viewsBuilderFactory.create(currentRoleIds)
                .ofType(ViewsType.MENU)
                .filterByPermission()
                .buildTree();
    }

    /**
     * 获取菜单和按钮树
     * @param userId 用户ID
     * @return 菜单和按钮树
     */
    @Override
    public List<ViewVO> getMenuAndButtonTree(Integer userId, Integer targetRoleId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);

        return viewsBuilderFactory.create(currentRoleIds, targetRoleId)
                .ofType(ViewsType.MENU_AND_BUTTON)
                .withChangeFlag()
                .withPermissionFlag()
                .buildTree();
    }
    /**
     * 获取目录树
     * @param userId 用户ID
     * @return 目录树
     */
    @Override
    public List<ViewVO> getDirectoryTree(Integer userId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return viewsBuilderFactory.create(currentRoleIds)
                .ofType(ViewsType.DIRECTORY)
                .filterByPermission()
                .buildTree();
    }

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    @Override
    public List<ViewVO> getViewVOS(Integer userId, String keyword) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return viewsBuilderFactory.create(currentRoleIds)
                .ofType(ViewsType.NOT_DIRECTORY_AND_BUTTON)
                .withKeyword(keyword)
                .filterByPermission()
                .build();
    }

    /**
     * 获取权限码列表
     * @param userId 用户ID
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionCode(Integer userId) {
        List<Integer> currentAndChildRoleIds = rolesBuilderFactory.createWithChildAndUser(userId).buildIds();
        return viewQueryDomain.getPermissionCode(currentAndChildRoleIds);
    }

    /**
     * 添加视图
     * @param userId 用户ID
     * @param viewDTO 视图DTO
     */
    @Override
    public void addView(Integer userId, ViewDTO viewDTO) {
        if (!viewValidationDomain.validateTypeComply(viewDTO.getParentNodeId(), viewDTO.getType())) {
            throw new BusinessException(ViewResultCode.VIEW_TYPE_NOT_COMPLY);
        }
        if (!Objects.equals(viewDTO.getParentNodeId(), ViewConstant.ROOT_VIEW_PARENT_ID)
                && !viewValidationDomain.validateViewContainUserViews(userId, viewDTO.getParentNodeId())) {
            throw new BusinessException(ViewResultCode.VIEW_NOT_AUTHORIZED_ADD);
        }
        viewOperationDomain.addView(viewDTO);
    }

    /**
     * 修改视图
     * @param userId 用户ID
     * @param viewDTO 视图DTO
     */
    @Override
    public void modifyView(Integer userId, ViewDTO viewDTO) {
        if (viewValidationDomain.validateParentIdEqualsSelfAndDescendants(viewDTO.getId(), viewDTO.getParentNodeId())) {
            throw new BusinessException(ViewResultCode.VIEW_CANNOT_BE_PARENT);
        }
        if (!viewValidationDomain.validateTypeComply(viewDTO.getParentNodeId(), viewDTO.getType())) {
            throw new BusinessException(ViewResultCode.VIEW_TYPE_NOT_COMPLY);
        }
        if (!viewValidationDomain.validateModifyValid(userId, viewDTO.getId(), viewDTO.getIsValid())) {
            throw new BusinessException(ViewResultCode.VIEW_CANNOT_MODIFY_VALID);
        }
        if (!viewValidationDomain.validateViewContainUserViews(userId, viewDTO.getId())) {
            throw new BusinessException(ViewResultCode.VIEW_NOT_AUTHORIZED_MODIFY);
        }
        viewOperationDomain.modifyView(viewDTO);
    }

    /**
     * 删除视图
     * @param userId 用户ID
     * @param viewId 视图ID
     */
    @Override
    public void deleteView(Integer userId, Integer viewId) {
        if (!viewValidationDomain.validateViewContainUserViews(userId, viewId)) {
            throw new BusinessException(ViewResultCode.VIEW_NOT_AUTHORIZED_DELETE);
        }
        viewOperationDomain.deleteView(viewId);
    }
    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     */
    @Override
    public void moveView(Integer viewId, Boolean isUp) {
        if (isUp && !viewValidationDomain.validateMoveUp(viewId)) {
            throw new BusinessException(ViewResultCode.VIEW_CANNOT_MOVE_UP);
        }
        if (!isUp && !viewValidationDomain.validateMoveDown(viewId)) {
            throw new BusinessException(ViewResultCode.VIEW_CANNOT_MOVE_DOWN);
        }
        viewOperationDomain.moveView(viewId, isUp);
    }

}
