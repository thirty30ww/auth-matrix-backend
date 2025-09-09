package com.thirty.user.model.vo;

import com.thirty.user.model.entity.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class RoleVO {
    private Role node;
    private Boolean hasPermission = false;
    private List<RoleVO> children = new ArrayList<>();

    /**
     * 构建角色VO映射
     */
    public static Map<Integer, RoleVO> buildMap(List<RoleVO> roleVOS) {
        return roleVOS.stream().collect(Collectors.toMap(
                roleVO -> roleVO.getNode().getId(),
                roleVO -> roleVO,
                (first, second) -> first, // 冲突时保留先出现的值
                LinkedHashMap::new
        ));
    }

    /**
     * 设置角色是否有权限
     * @param permittedRoleIds 权限角色ID列表
     * @param roleVOS 角色VO列表
     */
    public static void setHasPermission(List<Integer> permittedRoleIds, List<RoleVO> roleVOS) {
        roleVOS.forEach(roleVO -> {
            if (permittedRoleIds.contains(roleVO.getNode().getId())) {
                roleVO.setHasPermission(true);
            }
        });
    }

    /**
     * 过滤出角色树中有权限的角色
     * @param permittedRoleIds 权限角色ID列表
     * @param roleVOS 角色VO列表
     */
    public static void filterHasPermission(List<Integer> permittedRoleIds, List<RoleVO> roleVOS) {
        roleVOS.removeIf(roleVO -> !permittedRoleIds.contains(roleVO.getNode().getId()));
    }
}
