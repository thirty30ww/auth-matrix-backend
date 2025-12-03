package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.PermissionBkConverter;
import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.mapper.PermissionBkMapper;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.PermissionBkVO;
import com.thirty.user.service.basic.PermissionBkService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service实现
* @createDate 2025-08-03 10:06:39
*/
@Service
public class PermissionBkServiceImpl extends ServiceImpl<PermissionBkMapper, PermissionBk>
    implements PermissionBkService {

    /**
     * 根据类型和名称获取权限列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByTypeAndKeyword(PermissionBkType type, String keyword) {
        LambdaQueryWrapper<PermissionBk> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(PermissionBk::getType, type);
        }
        if (keyword != null) {
            wrapper.like(PermissionBk::getName, keyword);
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
    public List<PermissionBk> getPermissionByTypesAndKeyword(List<PermissionBkType> types, String keyword) {
        LambdaQueryWrapper<PermissionBk> wrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        wrapper.in(PermissionBk::getType, types)
                .orderByAsc(PermissionBk::getFrontId);
        if (keyword != null) {
            wrapper.like(PermissionBk::getName, keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据类型获取权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByType(PermissionBkType type) {
        return getPermissionByTypeAndKeyword(type, null);
    }

    /**
     * 根据类型列表获取权限列表
     * @param types 权限类型列表
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByTypes(List<PermissionBkType> types) {
        return getPermissionByTypesAndKeyword(types, null);
    }

    /**
     * 根据类型列表和名称获取权限VO列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    @Override
    public List<PermissionBkVO> getPermissionVOByTypesAndKeyword(List<PermissionBkType> types, String keyword) {
        List<PermissionBk> permissionBks = getPermissionByTypesAndKeyword(types, keyword);
        return PermissionBkConverter.INSTANCE.toPermissionVOS(permissionBks);
    }

    /**
     * 获取权限的所有祖先ID
     * @param permissionId 权限ID
     * @return 祖先ID列表
     */
    @Override
    public List<Integer> getAncestorIds(Integer permissionId) {
        List<Integer> ancestorIds = new ArrayList<>();
        List<PermissionBk> permissionBks = list();

        Map<Integer, PermissionBk> viewMap = PermissionBk.buildMap(permissionBks);

        PermissionBk currentPermissionBk = viewMap.get(permissionId);
        while (!Objects.equals(currentPermissionBk.getParentId(), RoleConstant.ROOT_ROLE_PARENT_ID)) {
            ancestorIds.add(currentPermissionBk.getParentId());
            currentPermissionBk = viewMap.get(currentPermissionBk.getParentId());
        }

        return ancestorIds;
    }

    /**
     * 获取权限列表的所有祖先ID
     * @param permissionIds 权限ID列表
     * @return 祖先ID列表
     */
    @Override
    public List<Integer> getAncestorIds(List<Integer> permissionIds) {
        Set<Integer> ancestorIds = new HashSet<>();
        for (Integer viewId : permissionIds) {
            ancestorIds.addAll(getAncestorIds(viewId));
        }
        return new ArrayList<>(ancestorIds);
    }

    /**
     * 获取权限的所有后代ID
     * @param permissionId 权限ID
     * @return 后代ID列表
     */
    @Override
    public List<Integer> getDescendantIds(Integer permissionId) {
        List<Integer> descendantIds = new ArrayList<>();
        List<PermissionBk> permissionBks = list();

        Map<Integer, List<PermissionBk>> parentChildMap = PermissionBk.buildParentChildMap(permissionBks);

        List<PermissionBk> currentLevel = parentChildMap.get(permissionId);
        while (!CollectionUtils.isEmpty(currentLevel)) {
            List<PermissionBk> nextLevel = new ArrayList<>();
            for (PermissionBk permissionBk : currentLevel) {
                descendantIds.add(permissionBk.getId());
                nextLevel.addAll(parentChildMap.getOrDefault(permissionBk.getId(), new ArrayList<>()));
            }
            currentLevel = nextLevel;
        }

        return descendantIds;
    }

    /**
     * 获取权限列表的所有后代ID
     * @param permissionIds 权限ID列表
     * @return 后代ID列表
     */
    @Override
    public List<Integer> getDescendantIds(List<Integer> permissionIds)  {
        Set<Integer> descendantIds = new HashSet<>();
        for (Integer viewId : permissionIds) {
            descendantIds.addAll(getDescendantIds(viewId));
        }
        return new ArrayList<>(descendantIds);
    }

    /**
     * 获取权限列表的所有权限码
     * @param permissionIds 权限ID列表
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionCodes(List<Integer> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return List.of();
        }
        LambdaQueryWrapper<PermissionBk> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PermissionBk::getId, permissionIds)
                .eq(PermissionBk::getIsValid, true)
                .eq(PermissionBk::getType, PermissionBkType.BUTTON);
        List<PermissionBk> permissionBks = list(wrapper);
        if (CollectionUtils.isEmpty(permissionBks)) {
            return List.of();
        }
        return PermissionBk.extractPermissionCodes(permissionBks);
    }

    /**
     * 修改权限
     * @param permissionBk 权限
     */
    @Override
    public void modifyPermission(PermissionBk permissionBk) {
        // 处理权限父节点变化
        PermissionBk oldPermissionBk = getById(permissionBk.getId());
        // 如果父节点变化，需要尾插法添加权限
        if (!Objects.equals(permissionBk.getParentId(), oldPermissionBk.getParentId())) {
            // 尾插法添加权限
            tailInsert(permissionBk);
            // 处理旧权限的邻居节点连接
            connectNeighborPermissions(oldPermissionBk);
            return;
        }

        // 如果权限状态从有效变为无效，需要更新所有后代权限
        if (!permissionBk.getIsValid() && oldPermissionBk.getIsValid()) {
            List<Integer> descendantIds = getDescendantIds(permissionBk.getId());
            List<PermissionBk> descendantPermissionBks = PermissionBk.toNotValidPermission(descendantIds);
            updateBatchById(descendantPermissionBks);
        }
        updateById(permissionBk);
    }

    /**
     * 尾插法添加权限
     * @param permissionBk 权限
     */
    @Override
    public void tailInsert(PermissionBk permissionBk) {
        // 获得父节点的尾节点
        PermissionBk tailPermissionBk = getTailNode(permissionBk.getParentId());

        // 处理permission的frontNodeId
        if (tailPermissionBk == null) {
            // 如果父节点没有尾节点，permission的frontNodeId为头节点ID
            permissionBk.setFrontId(PermissionConstant.HEAD_PERMISSION_FRONT_ID);
        } else {
            // 如果父节点有尾节点，permission的frontNodeId为尾节点ID
            permissionBk.setFrontId(tailPermissionBk.getId());
        }

        // 处理permission的behindNodeId
        permissionBk.setBehindId(PermissionConstant.TAIL_PERMISSION_BEHIND_ID);

        // 保存或更新权限
        if (permissionBk.getId() == null) {
            save(permissionBk);
        } else {
            updateById(permissionBk);
        }

        // 处理permission前一个权限的的behindNodeId
        if (tailPermissionBk != null) {
            tailPermissionBk.setBehindId(permissionBk.getId());
            updateById(tailPermissionBk);
        }
    }

    /**
     * 连接权限的邻居节点
     * @param permissionBk 权限
     */
    @Override
    public void connectNeighborPermissions(PermissionBk permissionBk) {
        // 将permission前后的邻居节点相互连接
        List<PermissionBk> neighborPermissionBks = new ArrayList<>();
        // 获取当前父节点下的所有权限
        Map<Integer, PermissionBk> viewMap = PermissionBk.buildMap(getByParentId(permissionBk.getParentId()));

        // 处理permission前一个权限的的behindNodeId
        if (!Objects.equals(permissionBk.getFrontId(), PermissionConstant.HEAD_PERMISSION_FRONT_ID)) {
            PermissionBk frontPermissionBk = viewMap.get(permissionBk.getFrontId());
            frontPermissionBk.setBehindId(permissionBk.getBehindId());
            neighborPermissionBks.add(frontPermissionBk);
        }
        // 处理permission后一个权限的frontNodeId
        if (!Objects.equals(permissionBk.getBehindId(), PermissionConstant.TAIL_PERMISSION_BEHIND_ID)) {
            PermissionBk behindPermissionBk = viewMap.get(permissionBk.getBehindId());
            behindPermissionBk.setFrontId(permissionBk.getFrontId());
            neighborPermissionBks.add(behindPermissionBk);
        }

        // 更新邻居节点的连接
        updateBatchById(neighborPermissionBks);
    }

    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    @Override
    public void deletePermission(Integer permissionId) {
        PermissionBk permissionBk = getById(permissionId);

        List<Integer> descendantIds = getDescendantIds(permissionId);
        descendantIds.add(permissionId);
        removeByIds(descendantIds);

        connectNeighborPermissions(permissionBk);
    }

    /**
     * 权限上移
     * @param permissionId 权限ID
     */
    @Override
    public void moveUp(Integer permissionId) {
        PermissionBk permissionBk = getById(permissionId);
        PermissionBk frontPermissionBk = getById(permissionBk.getFrontId());

        if (frontPermissionBk == null) {
            return;
        }

        frontPermissionBk.setBehindId(permissionBk.getBehindId());

        permissionBk.setFrontId(frontPermissionBk.getFrontId());
        permissionBk.setBehindId(frontPermissionBk.getId());

        frontPermissionBk.setFrontId(permissionId);

        List<PermissionBk> modifyPermissionBks = List.of(permissionBk, frontPermissionBk);
        updateBatchById(modifyPermissionBks);
    }

    /**
     * 权限下移
     * @param permissionId 权限ID
     */
    @Override
    public void moveDown(Integer permissionId) {
        PermissionBk permissionBk = getById(permissionId);
        PermissionBk behindPermissionBk = getById(permissionBk.getBehindId());

        if (behindPermissionBk == null) {
            return;
        }

        behindPermissionBk.setFrontId(permissionBk.getFrontId());

        permissionBk.setBehindId(behindPermissionBk.getBehindId());
        permissionBk.setFrontId(behindPermissionBk.getId());

        behindPermissionBk.setBehindId(permissionId);

        List<PermissionBk> modifyPermissionBks = List.of(permissionBk, behindPermissionBk);
        updateBatchById(modifyPermissionBks);
    }

    /**
     * 获取权限的尾节点
     * @param parentId 父节点ID
     * @return 尾节点
     */
    @Override
    public PermissionBk getTailNode(Integer parentId) {
        List<PermissionBk> permissionBks = getByParentId(parentId);
        return PermissionBk.extractMaxFrontIdPermission(permissionBks);
    }

    /**
     * 根据父节点ID获取权限列表
     * @param parentId 父节点ID
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getByParentId(Integer parentId) {
        LambdaQueryWrapper<PermissionBk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PermissionBk::getParentId, parentId);
        return list(wrapper);
    }
}