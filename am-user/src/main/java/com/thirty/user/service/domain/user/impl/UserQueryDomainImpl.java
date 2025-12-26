package com.thirty.user.service.domain.user.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.enums.model.DateRangeType;
import com.thirty.common.exception.BusinessException;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.vo.BaseChartVO;
import com.thirty.common.utils.DateRangeUtil;
import com.thirty.user.converter.UserConverter;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.dto.GetUsersDTO;
import com.thirty.user.model.entity.UserDetail;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.basic.UserDetailService;
import com.thirty.user.service.basic.UserOnlineService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.user.UserQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserQueryDomainImpl implements UserQueryDomain {

    @Resource
    private UserService userService;
    @Resource
    private UserDetailService userDetailService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserOnlineService userOnlineService;

    /**
     * 获取用户
     * @param currentUsername 当前用户名
     * @return 用户VO
     */
    @Override
    public UserVO getUser(String currentUsername) {
        // 获取用户
        User user = userService.getUser(currentUsername);

        // 获取用户详情
        UserDetail userDetail = userDetailService.getById(user.getId());

        // 获取用户角色
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());

        return UserConverter.INSTANCE.toUserVO(user, userDetail, roles);
    }

    /**
     * 获取用户
     * @param userId 用户ID
     * @return 用户VO
     */
    @Override
    public UserVO getUser(Integer userId) {
        // 获取用户
        User user = userService.getById(userId);

        // 校验用户是否存在
        if (!userService.validateUserExists(userId)) {
            throw new BusinessException(UserResultCode.USER_NOT_EXISTS);
        }

        // 获取用户详情
        UserDetail userDetail = userDetailService.getById(user.getId());

        // 获取用户角色
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());

        return UserConverter.INSTANCE.toUserVO(user, userDetail, roles);
    }

    /**
     * 获取用户列表
     * @param pageQueryDTO 获取用户列表请求参数
     * @param permittedRoleIds 有权限的角色ID列表
     * @param hasPermissionDisplay 是否仅显示有权限操作的用户
     * @return 用户列表
     */
    @Override
    public IPage<UserVO> getUsers(PageQueryDTO<GetUsersDTO> pageQueryDTO, List<Integer> permittedRoleIds, boolean hasPermissionDisplay) {
        // 获取基础用户列表
        IPage<UserVO> users = userService.getUsers(pageQueryDTO);

        // 设置用户列表中每个用户的hasPermission属性
        setUsersHasPermission(permittedRoleIds, users);

        // 是否仅显示有权限操作的用户
        if (hasPermissionDisplay) {
            // 过滤出有权限操作的用户
            filterUsersHasPermission(users);
        }

        return users;
    }

    /**
     * 获取在线用户列表
     * @return 在线用户列表
     */
    @Override
    public List<UserVO> getOnlineUsers() {
        List<Integer> userIds = userOnlineService.getOnlineUserIds();
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }

        List<User> users = userService.listByIds(userIds);
        List<UserDetail> userDetails = userDetailService.listByIds(userIds);
        return UserConverter.INSTANCE.toUserVOS(users, userDetails);
    }

    /**
     * 获取按时间单位分组的用户创建数量图表数据
     * @param type 时间范围类型
     * @return 按时间单位分组的用户创建数量图表数据
     */
    @Override
    public BaseChartVO<Integer, Long> getCreatedUserCountChart(DateRangeType type) {
        // 从工具类获取时间范围信息
        LocalDateTime now = LocalDateTime.now();
        DateRangeUtil.TimeRangeInfo timeRangeInfo = DateRangeUtil.getTimeRangeInfo(type, now);

        // 查询符合时间范围内创建的用户
        List<User> users = userService.listByCreateTimeBetween(timeRangeInfo.startTime(), timeRangeInfo.endTime());

        // 按时间单位分组统计用户数量
        Map<Integer, Long> userCountByTimeUnit = users.stream()
                .collect(Collectors.groupingBy(
                        user -> timeRangeInfo.groupingFunction().apply(user.getCreateTime()),
                        Collectors.counting()
                ));

        // 转换为图表VO
        List<Integer> allTimeUnits = DateRangeUtil.getAllTimeUnits(type, now);
        return new BaseChartVO<>(
                allTimeUnits,
                allTimeUnits.stream()
                        .map(timeUnit -> userCountByTimeUnit.getOrDefault(timeUnit, 0L))
                        .collect(Collectors.toList())
        );
    }

     /**
     * 获取最近两天的用户创建数量
     * @return 最近两天的用户创建数量
     */
     @Override
     public Map<String, Long> getLastTwoDayCreatedUserCount() {
        LocalDateTime now = LocalDateTime.now();
        // 获取今天创建用户数
        LocalDateTime startOfToday = DateRangeUtil.getTodayStart(now);
        Long todayCreatedUserCount = userService.getCreatedUserCount(startOfToday, now);

        // 获取昨天创建用户数
        LocalDateTime startOfYesterday = startOfToday.minusDays(1);
        Long yesterdayCreatedUserCount = userService.getCreatedUserCount(startOfYesterday, startOfToday);

        // 转换为Map
        return Map.of(
                "today", todayCreatedUserCount,
                "yesterday", yesterdayCreatedUserCount
        );
    }

    /**
     * 验证用户列表中每个用户是否有指定的权限
     * @param permittedRoleIds 权限角色ID列表
     * @param users 用户列表
     */
    private void setUsersHasPermission(List<Integer> permittedRoleIds, IPage<UserVO> users) {
        // 遍历每个UserVO，检查权限
        for (UserVO userVO : users.getRecords()) {
            // 获取列表用户的角色
            List<Role> roles = userVO.getRoles();

            // 获取列表用户的角色ID
            List<Integer> userRoleIds = Role.extractIds(roles);

            // 如果用户的所有角色ID都包含在权限角色列表中，则hasPermission为true
            boolean hasPermission = new HashSet<>(permittedRoleIds).containsAll(userRoleIds);
            userVO.setHasPermission(hasPermission);
        }
    }

    /**
     * 过滤出用户列表中有权限的用户
     * @param users 用户列表
     */
    private void filterUsersHasPermission(IPage<UserVO> users) {
        users.getRecords().removeIf(userVO -> !userVO.getHasPermission());
    }
}
