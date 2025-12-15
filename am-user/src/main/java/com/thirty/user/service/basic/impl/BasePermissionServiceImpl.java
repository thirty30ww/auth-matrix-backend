package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.base.BasePermission;
import com.thirty.user.service.basic.BasePermissionService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基础权限服务实现类
 * @param <M> 基础权限映射器类型
 * @param <T> 基础权限实体类型
 */
public class BasePermissionServiceImpl<
        M extends BaseMapper<T>,
        T extends BasePermission
        >
        extends ServiceImpl<M, T>
        implements BasePermissionService<T> {

    /**
     * 根据权限类型和关键字查询权限列表
     * @param type 权限类型
     * @param keyword 权限名称关键字
     * @return 权限列表
     */
    @Override
    public <E extends PermissionType> List<T> getPermissionByTypeAndKeyword(E type, String keyword) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.eq("type", type);
        }
        if (keyword != null) {
            wrapper.like("name", keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据权限类型列表和关键字查询权限列表
     * @param types 权限类型列表
     * @param keyword 权限名称关键字
     * @return 权限列表
     */
    @Override
    public <E extends PermissionType> List<T> getPermissionByTypesAndKeyword(List<E> types, String keyword) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (CollectionUtils.isEmpty(types)) {
            return List.of();
        }
        wrapper.in("type", types);
        wrapper.orderByAsc("parent_id")
                .orderByAsc("front_id");
        if (keyword != null) {
            wrapper.like("name", keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据权限类型查询权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    @Override
    public <E extends PermissionType> List<T> getPermissionByTypes(E type) {
        return getPermissionByTypeAndKeyword(type, null);
    }

    /**
     * 根据权限类型列表查询权限列表
     * @param types 权限类型列表
     * @return 权限列表
     */
    @Override
    public <E extends PermissionType> List<T> getPermissionByTypes(List<E> types) {
        return getPermissionByTypesAndKeyword(types, null);
    }

    /**
     * 获取指定权限ID的所有祖先权限ID（不包含当前权限）
     * @param permissionId 权限ID
     * @return 所有祖先权限ID列表
     */
    @Override
    public List<Integer> getAncestorIds(Integer permissionId) {
        List<Integer> ancestorIds = new ArrayList<>();
        T permission = getById(permissionId);
        while (Objects.nonNull(permission) && !Objects.equals(permission.getParentId(), PermissionConstant.ROOT_PERMISSION_PARENT_ID)) {
            ancestorIds.add(permission.getParentId());
            permission = getById(permission.getParentId());
        }
        return ancestorIds;
    }

    /**
     * 获取指定权限ID列表的所有祖先权限ID（不包含当前权限）
     * @param permissionIds 权限ID列表
     * @return 所有祖先权限ID列表
     */
    @Override
    public List<Integer> getAncestorIds(List<Integer> permissionIds) {
        return permissionIds.stream()
                .map(this::getAncestorIds)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定权限ID的所有后代权限ID
     * @param permissionId 权限ID
     * @return 所有后代权限ID列表
     */
    @Override
    public List<Integer> getDescendantIds(Integer permissionId) {
        List<Integer> descendantIds = new ArrayList<>();
        List<T> permissions = list();

        Map<Integer, List<T>> parentChildMap = T.buildParentChildMap(permissions);

        List<T> currentLevel = parentChildMap.get(permissionId);
        while (!CollectionUtils.isEmpty(currentLevel)) {
            List<T> nextLevel = new ArrayList<>();
            for (T permission : currentLevel) {
                descendantIds.add(permission.getId());
                nextLevel.addAll(parentChildMap.getOrDefault(permission.getId(), new ArrayList<>()));
            }
            currentLevel = nextLevel;
        }
        return descendantIds;
    }

    /**
     * 获取指定权限ID列表的所有后代权限ID
     * @param permissionIds 权限ID列表
     * @return 所有后代权限ID列表
     */
    @Override
    public List<Integer> getDescendantIds(List<Integer> permissionIds) {
        return permissionIds.stream()
                .map(this::getDescendantIds)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定权限ID列表的所有权限码
     * @param permissionIds 权限ID列表
     * @return 所有权限码列表
     */
    @Override
    public List<String> getPermissionCodes(List<Integer> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return List.of();
        }
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.in("id", permissionIds)
                .eq("is_valid", true);
        List<T> permissions = list(wrapper);
        return T.extractPermissionCodes(permissions);
    }

    /**
     * 在指定权限之后插入权限
     * @param permission 权限实体
     * @param frontPermission 前一个权限实体
     */
    @Override
    public void insert(T permission, T frontPermission) {
        Integer permissionId = saveOrUpdatePermission(permission);  // 保存或更新权限，获取权限ID

        T behindPermission = getById(frontPermission.getBehindId());    // 获取frontPermission的behindPermission

        frontPermission.setBehindId(permissionId);
        permission.setFrontId(frontPermission.getId());

        if (Objects.nonNull(behindPermission)) {    // 若frontPermission有behindPermission
            behindPermission.setFrontId(permissionId);
            permission.setBehindId(behindPermission.getId());
            updateBatchById(List.of(frontPermission, behindPermission, permission));
        } else {
            permission.setBehindId(PermissionConstant.TAIL_PERMISSION_BEHIND_ID);
            updateBatchById(List.of(frontPermission, permission));
        }
    }

    /**
     * 在指定父节点的前头插入权限
     * @param permission 权限实体
     */
    @Override
    public void headInsert(T permission) {
        Integer permissionId = saveOrUpdatePermission(permission);
        permission.setFrontId(PermissionConstant.HEAD_PERMISSION_FRONT_ID);

        T headPermission = getHeadNode(permission.getParentId());   // 获取指定父节点的头子节点

        if(Objects.isNull(headPermission)) {
            permission.setBehindId(PermissionConstant.TAIL_PERMISSION_BEHIND_ID);
            updateById(permission);
        } else {
            headPermission.setFrontId(permissionId);
            permission.setBehindId(headPermission.getBehindId());
            updateBatchById(List.of(headPermission, permission));
        }
    }

    /**
     * 尾插法插入权限
     * @param permission 权限实体
     */
    @Override
    public void tailInsert(T permission) {
        T tailPermission = getTailNode(permission.getParentId());   // 获取指定父节点的尾子节点

        if (Objects.isNull(tailPermission)) {   // 如果父节点下没有子节点，直接头插法添加权限
            headInsert(permission);
        } else {    // 否则在tailPermission之后插入permission
            insert(permission, tailPermission);
        }

    }

    /**
     * 连接权限的邻居节点
     * @param permission 权限实体
     */
    @Override
    public void connectNeighborPermissions(T permission) {
        // 将permission前后的邻居节点相互连接
        List<T> neighborPermissions = new ArrayList<>();
        // 获取当前父节点下的所有权限
        Map<Integer, T> permissionMap = T.buildMap(getByParentId(permission.getParentId()));

        // 处理permission前一个权限的behindId
        if (!Objects.equals(permission.getFrontId(), PermissionConstant.HEAD_PERMISSION_FRONT_ID)) {
            T frontPermission = permissionMap.get(permission.getFrontId());
            frontPermission.setBehindId(permission.getBehindId());
            neighborPermissions.add(frontPermission);
        }
        // 处理permission后一个权限的frontId
        if (!Objects.equals(permission.getBehindId(), PermissionConstant.TAIL_PERMISSION_BEHIND_ID)) {
            T behindPermission = permissionMap.get(permission.getBehindId());
            behindPermission.setFrontId(permission.getFrontId());
            neighborPermissions.add(behindPermission);
        }

        // 更新邻居节点
        updateBatchById(neighborPermissions);
    }

    /**
     * 修改权限
     * @param permission 权限实体
     */
    @Override
    public void modifyPermission(T permission) {
        T oldPermission = getById(permission.getId());  // 处理权限父节点变化
        if (!Objects.equals(permission.getParentId(), oldPermission.getParentId())) {   // 如果父节点变化，需要尾插法添加权限
            tailInsert(permission); // 尾插法添加权限
            connectNeighborPermissions(oldPermission);  // 处理旧权限的邻居节点连接
            return;
        }

        if (!permission.getIsValid() && oldPermission.getIsValid()) {   // 如果权限状态从有效变为无效，需要更新所有后代权限
            List<Integer> descendantIds = getDescendantIds(permission.getId()); // 获取所有后代权限ID
            List<T> descendantPermissions = T.toNotValidPermission(getEntityClass(), descendantIds);    // 将所有后代权限ID转换为无效权限列表
            updateBatchById(descendantPermissions); // 更新所有后代权限
        }
        updateById(permission);
    }

    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    @Override
    public void deletePermission(Integer permissionId) {
        T permission = getById(permissionId);
        List<Integer> descendantIds = getDescendantIds(permissionId); // 获取所有后代权限ID
        descendantIds.add(permissionId);
        removeByIds(descendantIds); // 删除所有后代权限

        connectNeighborPermissions(permission); // 连接权限的邻居节点
    }

    /**
     * 权限上移
     * @param permissionId 权限ID
     */
    @Override
    public void moveUp(Integer permissionId) {
        T permission = getById(permissionId);
        T frontPermission = getById(permission.getFrontId());
        T targetPermission = getById(frontPermission.getFrontId());

        connectNeighborPermissions(permission);

        if (Objects.isNull(targetPermission)) {
            headInsert(permission);
        } else {
            insert(permission, targetPermission);
        }
    }

    /**
     * 权限下移
     * @param permissionId 权限ID
     */
    @Override
    public void moveDown(Integer permissionId) {
        T permission = getById(permissionId);
        connectNeighborPermissions(permission);

        T behindPermission = getById(permission.getBehindId());
        insert(permission, behindPermission);
    }

    /**
     * 保存或更新权限
     * @param permission 权限实体
     * @return 权限ID
     */
    private Integer saveOrUpdatePermission(T permission) {
        if (permission.getId() == null) {   // 若权限ID为空，执行保存操作
            save(permission);
        } else {    // 若权限ID不为空，执行更新操作
            updateById(permission);
        }
        return permission.getId();
    }

    /**
     * 获取指定父节点ID的头节点
     * @param parentId 父节点ID
     * @return 头节点
     */
    private T getHeadNode(Integer parentId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId)
                .eq("front_id", PermissionConstant.HEAD_PERMISSION_FRONT_ID);
        return getOne(wrapper);
    }

    /**
     * 获取指定父节点ID的最后一个子节点
     * @param parentId 父节点ID
     * @return 最后一个子节点
     */
    private T getTailNode(Integer parentId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId)
                .eq("behind_id", PermissionConstant.TAIL_PERMISSION_BEHIND_ID);
        return getOne(wrapper);
    }

    /**
     * 获取指定父节点ID的所有子节点
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    private List<T> getByParentId(Integer parentId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        return list(wrapper);
    }
}
