package com.thirty.user.controller;

import com.thirty.common.annotation.OperateLog;
import com.thirty.common.annotation.OperateModule;
import com.thirty.common.enums.model.DateRangeType;
import com.thirty.common.enums.model.OperationType;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.common.model.vo.BaseChartVO;
import com.thirty.user.enums.result.StatisticResultCode;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.service.facade.StatisticFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
@OperateModule("数据统计")
public class StatisticController {
    @Resource
    private StatisticFacade statisticFacade;

    /**
     * 获取在线用户
     * @return 在线用户列表
     */
    @GetMapping("/online-users")
    @OperateLog(type = OperationType.SELECT, description = "获取在线用户")
    public ResultDTO<List<UserVO>> getOnlineUsers() {
        return ResultDTO.of(StatisticResultCode.GET_ONLINE_USERS_SUCCESS, statisticFacade.getOnlineUsers());
    }

     /**
     * 获取按时间单位分组的用户创建数量图表数据
     * @param type 时间范围类型
     * @return 按时间单位分组的用户创建数量图表数据
     */
    @GetMapping("/created-user-count-chart")
    @OperateLog(type = OperationType.SELECT, description = "获取创建用户数量图表数据")
    public ResultDTO<BaseChartVO<Integer, Long>> getCreatedUserCountChart(DateRangeType type) {
        return ResultDTO.of(StatisticResultCode.GET_CREATED_USER_COUNT_SUCCESS, statisticFacade.getCreatedUserCountChart(type));
    }

    /**
     * 获取最近两天的用户创建数量
     * @return 最近两天的用户创建数量
     */
    @GetMapping("/created-user-count/day")
    @OperateLog(type = OperationType.SELECT, description = "获取最近两天的用户创建数量")
    public ResultDTO<Map<String, Long>> getLastTwoDayCreatedUserCount() {
        return ResultDTO.of(StatisticResultCode.GET_LAST_TWO_DAY_CREATED_USER_COUNT_SUCCESS, statisticFacade.getLastTwoDayCreatedUserCount());
    }

    /**
     * 获取最近两天的异常操作次数
     * @return 最近两天的异常操作次数
     */
    @GetMapping("/abnormal-operation-count/day")
    @OperateLog(type = OperationType.SELECT, description = "获取最近两天的异常操作次数")
    public ResultDTO<Map<String, Long>> getLastTwoDayAbnormalOperationCount() {
        return ResultDTO.of(StatisticResultCode.GET_LAST_TWO_DAY_ABNORMAL_OPERATION_COUNT_SUCCESS, statisticFacade.getLastTwoDayAbnormalOperationCount());
    }
}
