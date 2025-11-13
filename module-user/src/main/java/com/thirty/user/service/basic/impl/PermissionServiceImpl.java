package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.converter.PermissionConverter;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.mapper.ViewMapper;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.model.vo.PermissionVO;
import com.thirty.user.service.basic.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service实现
* @createDate 2025-08-03 10:06:39
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<ViewMapper, Permission>
    implements PermissionService {

    /**
     * 根据类型和名称获取权限列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限列表
     */
    @Override
    public List<Permission> getPermissionByTypeAndKeyword(PermissionType type, String keyword) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.eq("type", type);
        }
        if (keyword != null) {
            wrapper.like("name", keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据类型列表和名称获取权限列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限列表
     */
    @Override
    public List<Permission> getPermissionByTypesAndKeyword(List<PermissionType> types, String keyword) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        wrapper.in("type", types)
                .orderByAsc("front_node_id");
        if (keyword != null) {
            wrapper.like("name", keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据查询条件获取权限列表
     * @param wrapper 查询条件
     * @return 权限列表
     */
    @Override
    public List<Permission> getPermissionByWrapper(QueryWrapper<Permission> wrapper) {
        return list(wrapper);
    }

    /**
     * 根据类型获取权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    @Override
    public List<Permission> getPermissionByType(PermissionType type) {
        return getPermissionByTypeAndKeyword(type, null);
    }

    /**
     * 根据类型列表获取权限列表
     * @param types 权限类型列表
     * @return 权限列表
     */
    @Override
    public List<Permission> getPermissionByTypes(List<PermissionType> types) {
        return getPermissionByTypesAndKeyword(types, null);
    }

    /**
     * 根据类型和名称获取权限VO列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    @Override
    public List<PermissionVO> getPermissionVOByTypeAndKeyword(PermissionType type, String keyword) {
        List<Permission> permissions = getPermissionByTypeAndKeyword(type, keyword);
        return PermissionConverter.INSTANCE.toPermissionVOS(permissions);
    }

    /**
     * 根据类型列表和名称获取权限VO列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    @Override
    public List<PermissionVO> getPermissionVOByTypesAndKeyword(List<PermissionType> types, String keyword) {
        List<Permission> permissions = getPermissionByTypesAndKeyword(types, keyword);
        return PermissionConverter.INSTANCE.toPermissionVOS(permissions);
    }

    /**
     * 根据查询条件获取权限VO列表
     * @param wrapper 查询条件
     * @return 权限VO列表
     */
    @Override
    public List<PermissionVO> getPermissionVOByWrapper(QueryWrapper<Permission> wrapper) {
        List<Permission> permissions = list(wrapper);
        return PermissionConverter.INSTANCE.toPermissionVOS(permissions);
    }

    /**
     * 根据类型获取权限VO列表
     * @param type 权限类型
     * @return 权限VO列表
     */
    @Override
    public List<PermissionVO> getPermissionVOByType(PermissionType type) {
        List<Permission> permissions = getPermissionByType(type);
        return PermissionConverter.INSTANCE.toPermissionVOS(permissions);
    }

    /**
     * 根据类型列表获取权限VO列表
     * @param types 权限类型列表
     * @return 权限VO列表
     */
    @Override
    public List<PermissionVO> getPermissionVOByTypes(List<PermissionType> types) {
        List<Permission> permissions = getPermissionByTypes(types);
        return PermissionConverter.INSTANCE.toPermissionVOS(permissions);
    }

    /**
     * 获取权限的所有祖先ID
     * @param viewId 权限ID
     * @return 祖先ID列表
     */
    @Override
    public List<Integer> getAncestorIds(Integer viewId) {
        List<Integer> ancestorIds = new ArrayList<>();
        List<Permission> permissions = list();

        Map<Integer, Permission> viewMap = Permission.buildMap(permissions);

        Permission currentPermission = viewMap.get(viewId);
        while (!Objects.equals(currentPermission.getParentNodeId(), RoleConstant.ROOT_ROLE_PARENT_ID)) {
            ancestorIds.add(currentPermission.getParentNodeId());
            currentPermission = viewMap.get(currentPermission.getParentNodeId());
        }

        return ancestorIds;
    }

    /**
     * 获取权限列表的所有祖先ID
     * @param viewIds 权限ID列表
     * @return 祖先ID列表
     */
    @Override
    public List<Integer> getAncestorIds(List<Integer> viewIds) {
        Set<Integer> ancestorIds = new HashSet<>();
        for (Integer viewId : viewIds) {
            ancestorIds.addAll(getAncestorIds(viewId));
        }
        return new ArrayList<>(ancestorIds);
    }

    /**
     * 获取权限的所有后代ID
     * @param viewId 权限ID
     * @return 后代ID列表
     */
    @Override
    public List<Integer> getDescendantIds(Integer viewId) {
        List<Integer> descendantIds = new ArrayList<>();
        List<Permission> permissions = list();

        Map<Integer, List<Permission>> parentChildMap = Permission.buildParentChildMap(permissions);

        List<Permission> currentLevel = parentChildMap.get(viewId);
        while (!CollectionUtils.isEmpty(currentLevel)) {
            List<Permission> nextLevel = new ArrayList<>();
            for (Permission permission : currentLevel) {
                descendantIds.add(permission.getId());
                nextLevel.addAll(parentChildMap.getOrDefault(permission.getId(), new ArrayList<>()));
            }
            currentLevel = nextLevel;
        }

        return descendantIds;
    }

    /**
     * 获取权限列表的所有后代ID
     * @param viewIds 权限ID列表
     * @return 后代ID列表
     */
    @Override
    public List<Integer> getDescendantIds(List<Integer> viewIds)  {
        Set<Integer> descendantIds = new HashSet<>();
        for (Integer viewId : viewIds) {
            descendantIds.addAll(getDescendantIds(viewId));
        }
        return new ArrayList<>(descendantIds);
    }

    /**
     * 获取权限列表的所有权限码
     * @param viewIds 权限ID列表
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionCodes(List<Integer> viewIds) {
        if (CollectionUtils.isEmpty(viewIds)) {
            return List.of();
        }
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.in("id", viewIds)
                .eq("is_valid", true)
                .eq("type", PermissionType.BUTTON);
        List<Permission> permissions = list(wrapper);
        if (CollectionUtils.isEmpty(permissions)) {
            return List.of();
        }
        return Permission.extractPermissionCodes(permissions);
    }

    /**
     * 修改权限
     * @param permission 权限
     */
    @Override
    public void modifyPermission(Permission permission) {
        // 处理权限父节点变化
        Permission oldPermission = getById(permission.getId());
        // 如果父节点变化，需要尾插法添加权限
        if (!Objects.equals(permission.getParentNodeId(), oldPermission.getParentNodeId())) {
            // 尾插法添加权限
            tailInsert(permission);
            // 处理旧权限的邻居节点连接
            connectNeighborPermissions(oldPermission);
            return;
        }

        // 如果权限状态从有效变为无效，需要更新所有后代权限
        if (!permission.getIsValid() && oldPermission.getIsValid()) {
            List<Integer> descendantIds = getDescendantIds(permission.getId());
            List<Permission> descendantPermissions = Permission.toNotValidPermission(descendantIds);
            updateBatchById(descendantPermissions);
        }
        updateById(permission);
    }

    /**
     * 尾插法添加权限
     * @param permission 权限
     */
    @Override
    public void tailInsert(Permission permission) {
        // 获得父节点的尾节点
        Permission tailPermission = getTailNode(permission.getParentNodeId());

        // 处理permission的frontNodeId
        if (tailPermission == null) {
            // 如果父节点没有尾节点，permission的frontNodeId为头节点ID
            permission.setFrontNodeId(PermissionConstant.HEAD_PERMISSION_FRONT_ID);
        } else {
            // 如果父节点有尾节点，permission的frontNodeId为尾节点ID
            permission.setFrontNodeId(tailPermission.getId());
        }

        // 处理permission的behindNodeId
        permission.setBehindNodeId(PermissionConstant.TAIL_PERMISSION_BEHIND_ID);

        // 保存或更新权限
        if (permission.getId() == null) {
            save(permission);
        } else {
            updateById(permission);
        }

        // 处理permission前一个权限的的behindNodeId
        if (tailPermission != null) {
            tailPermission.setBehindNodeId(permission.getId());
            updateById(tailPermission);
        }
    }

    /**
     * 连接权限的邻居节点
     * @param permission 权限
     */
    @Override
    public void connectNeighborPermissions(Permission permission) {
        // 将permission前后的邻居节点相互连接
        List<Permission> neighborPermissions = new ArrayList<>();
        // 获取当前父节点下的所有权限
        Map<Integer, Permission> viewMap = Permission.buildMap(getByParentId(permission.getParentNodeId()));

        // 处理permission前一个权限的的behindNodeId
        if (!Objects.equals(permission.getFrontNodeId(), PermissionConstant.HEAD_PERMISSION_FRONT_ID)) {
            Permission frontPermission = viewMap.get(permission.getFrontNodeId());
            frontPermission.setBehindNodeId(permission.getBehindNodeId());
            neighborPermissions.add(frontPermission);
        }
        // 处理permission后一个权限的frontNodeId
        if (!Objects.equals(permission.getBehindNodeId(), PermissionConstant.TAIL_PERMISSION_BEHIND_ID)) {
            Permission behindPermission = viewMap.get(permission.getBehindNodeId());
            behindPermission.setFrontNodeId(permission.getFrontNodeId());
            neighborPermissions.add(behindPermission);
        }

        // 更新邻居节点的连接
        updateBatchById(neighborPermissions);
    }

    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    @Override
    public void deletePermission(Integer permissionId) {
        Permission permission = getById(permissionId);

        List<Integer> descendantIds = getDescendantIds(permissionId);
        descendantIds.add(permissionId);
        removeByIds(descendantIds);

        connectNeighborPermissions(permission);
    }

    /**
     * 权限上移
     * @param permissionId 权限ID
     */
    @Override
    public void moveUp(Integer permissionId) {
        Permission permission = getById(permissionId);
        Permission frontPermission = getById(permission.getFrontNodeId());

        if (frontPermission == null) {
            return;
        }

        frontPermission.setBehindNodeId(permission.getBehindNodeId());

        permission.setFrontNodeId(frontPermission.getFrontNodeId());
        permission.setBehindNodeId(frontPermission.getId());

        frontPermission.setFrontNodeId(permissionId);

        List<Permission> modifyPermissions = List.of(permission, frontPermission);
        updateBatchById(modifyPermissions);
    }

    /**
     * 权限下移
     * @param permissionId 权限ID
     */
    @Override
    public void moveDown(Integer permissionId) {
        Permission permission = getById(permissionId);
        Permission behindPermission = getById(permission.getBehindNodeId());

        if (behindPermission == null) {
            return;
        }

        behindPermission.setFrontNodeId(permission.getFrontNodeId());

        permission.setBehindNodeId(behindPermission.getBehindNodeId());
        permission.setFrontNodeId(behindPermission.getId());

        behindPermission.setBehindNodeId(permissionId);

        List<Permission> modifyPermissions = List.of(permission, behindPermission);
        updateBatchById(modifyPermissions);
    }

    /**
     * 获取权限的尾节点
     * @param parentId 父节点ID
     * @return 尾节点
     */
    @Override
    public Permission getTailNode(Integer parentId) {
        List<Permission> permissions = getByParentId(parentId);
        return Permission.extractMaxFrontIdPermission(permissions);
    }

    /**
     * 根据父节点ID获取权限列表
     * @param parentId 父节点ID
     * @return 权限列表
     */
    @Override
    public List<Permission> getByParentId(Integer parentId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_node_id", parentId);
        return list(wrapper);
    }
}




