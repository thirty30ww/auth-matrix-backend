package com.thirty.user.service.facade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogOperationVO;

import java.util.List;

public interface LogFacade {
    /**
     * 获取日志操作列表
     * @param pageQueryDTO 分页查询参数
     * @return 日志操作列表
     */
    IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO);

    /**
     * 获取日志操作详情
     * @param id 日志操作ID
     * @return 日志操作详情
     */
    LogOperationVO getLogOperation(Integer id);

    /**
     * 获取日志操作的代码列表
     * @return 代码列表
     */
    List<Integer> getCodes();

    /**
     * 获取日志操作的模块列表
     * @return 模块列表
     */
    List<String> getModules();
}
