package com.thirty.system.api;

public interface SettingApi {
    /**
     * 是否仅显示有权限操作的数据
     * @return 设置值
     */
    boolean hasPermissionDisplay();

    /**
     * 获取限制的用户角色数量
     * @return 限制的用户角色数量
     */
    Integer getUserRoleNumberLimit();
}
