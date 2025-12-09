package com.thirty.user.model.vo;

import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.base.BasePermissionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionBkVO extends BasePermissionVO<PermissionBk, PermissionBkVO> {

    /**
     * 过滤出菜单树中含有 permittedPermissionIds 的权限
     * @param permittedPermissionIds 有权限的权限ID列表
     * @param menus 菜单列表
     */
    public static void filterHasPermission(List<Integer> permittedPermissionIds, List<PermissionBkVO> menus) {
        menus.removeIf(permission -> permission.getNode().getType() != PermissionBkType.PAGE && !permittedPermissionIds.contains(permission.getNode().getId()));
    }
}
