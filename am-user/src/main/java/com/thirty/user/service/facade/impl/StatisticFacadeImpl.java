package com.thirty.user.service.facade.impl;

import com.thirty.common.enums.model.DateRangeType;
import com.thirty.common.model.vo.BaseChartVO;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.domain.Log.LogDomain;
import com.thirty.user.service.domain.user.UserQueryDomain;
import com.thirty.user.service.facade.StatisticFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticFacadeImpl implements StatisticFacade {
    @Resource
    private UserQueryDomain userQueryDomain;
    @Resource
    private LogDomain logDomain;

    /**
     * 获取在线用户列表
     * @return 在线用户列表
     */
    @Override
    public List<UserVO> getOnlineUsers() {
        return userQueryDomain.getOnlineUsers();
    }

    /**
     * 获取按时间单位分组的用户创建数量图表数据
     * @param type 时间范围类型
     * @return 按时间单位分组的用户创建数量图表数据
     */
    @Override
    public BaseChartVO<Integer, Long> getCreatedUserCountChart(DateRangeType type) {
        return userQueryDomain.getCreatedUserCountChart(type);
    }

    /**
     * 获取最近两天的用户创建数量
     * @return 最近两天的用户创建数量
     */
     @Override
    public Map<String, Long> getLastTwoDayCreatedUserCount() {
        return userQueryDomain.getLastTwoDayCreatedUserCount();
    }

     /**
      * 获取最近两天的异常操作次数
      * @return 最近两天的异常操作次数
      */
     @Override
     public Map<String, Long> getLastTwoDayAbnormalOperationCount() {
        return logDomain.getLastTwoDayAbnormalOperationCount();
     }
}
