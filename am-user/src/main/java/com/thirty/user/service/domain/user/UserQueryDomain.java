package com.thirty.user.service.domain.user;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.enums.model.DateRangeType;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.vo.BaseChartVO;
import com.thirty.user.model.dto.GetUsersDTO;
import com.thirty.user.model.vo.UserVO;

import java.util.List;
import java.util.Map;

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
     * @param dto 获取用户列表请求参数
     * @param permittedRoleIds 有权限的角色ID列表
     * @param hasPermissionDisplay 是否仅显示有权限操作的用户
     * @return 用户列表
     */
    IPage<UserVO> getUsers(PageQueryDTO<GetUsersDTO> dto, List<Integer> permittedRoleIds, boolean hasPermissionDisplay);

    /**
     * 获取在线用户列表
     * @return 在线用户列表
     */
    List<UserVO> getOnlineUsers();

    /**
     * 获取按时间单位分组的用户创建数量图表数据
     * @param type 时间范围类型
     * @return 按时间单位分组的用户创建数量图表数据
     */
    BaseChartVO<Integer, Long> getCreatedUserCountChart(DateRangeType type);

    /**
     * 获取最近两天的用户创建数量
     * @return 最近两天的用户创建数量
     */
    Map<String, Long> getLastTwoDayCreatedUserCount();
}
