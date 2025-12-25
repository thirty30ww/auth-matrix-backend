package com.thirty.user.service.facade;

import com.thirty.common.enums.model.DataRangeType;
import com.thirty.common.model.vo.BaseChartVO;
import com.thirty.user.model.vo.UserVO;

import java.util.List;
import java.util.Map;

public interface StatisticFacade {
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
    BaseChartVO<Integer, Long> getCreatedUserCount(DataRangeType type);

     /**
     * 获取最近两天的用户创建数量
     * @return 最近两天的用户创建数量
     */
    Map<String, Long> getLastTwoDayCreatedUserCount();

    /**
     * 获取最近两天的异常操作次数
     * @return 最近两天的异常操作次数
     */
    Map<String, Long> getLastTwoDayAbnormalOperationCount();
}
