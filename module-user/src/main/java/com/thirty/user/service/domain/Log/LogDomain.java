package com.thirty.user.service.domain.Log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogOperationVO;

import java.util.List;

public interface LogDomain {
    /**
     * 获取日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 日志列表
     */
    IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO);

    /**
     * 获取日志详情
     * @param id 操作日志ID
     * @return 操作日志详情
     */
    LogOperationVO getLogOperation(Integer id);
}
