package com.thirty.user.service.domain.user;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.GetUsersDTO;
import com.thirty.user.model.vo.UserVO;

import java.util.List;

public interface UserQueryDomain {
    /**
     * 获取用户
     * @param currentUsername 当前用户名
     * @return 用户VO
     */
    UserVO getUser(String currentUsername);

    /**
     * 获取用户
     * @param userId 用户ID
     * @return 用户VO
     */
    UserVO getUser(Integer userId);

    /**
     * 获取用户列表
     * @param pageQueryDTO 获取用户列表请求参数
     * @param permittedRoleIds 有权限的角色ID列表
     * @param hasPermissionDisplay 是否仅显示有权限操作的用户
     * @return 用户列表
     */
    IPage<UserVO> getUsers(PageQueryDTO<GetUsersDTO> pageQueryDTO, List<Integer> permittedRoleIds, boolean hasPermissionDisplay);
}
