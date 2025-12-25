package com.thirty.user.service.domain.Log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogLoginVO;
import com.thirty.user.model.vo.LogOperationVO;

import java.util.List;
import java.util.Map;

public interface LogDomain {
    /**
     * 获取日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 日志列表
     */
    IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO);

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 登录日志列表
     */
    IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO);

    /**
     * 获取日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 角色ID列表
     * @return 日志列表
     */
    IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO, List<Integer> permittedRoleIds);

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 权限角色ID列表
     * @return 登录日志列表
     */
    IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO, List<Integer> permittedRoleIds);

    /**
     * 获取日志详情
     * @param id 操作日志ID
     * @return 操作日志详情
     */
    LogOperationVO getLogOperation(Integer id);

    /**
     * 获取最近两天的异常操作次数
     * @return 最近两天的异常操作次数
     */
    Map<String, Long> getLastTwoDayAbnormalOperationCount();
}
