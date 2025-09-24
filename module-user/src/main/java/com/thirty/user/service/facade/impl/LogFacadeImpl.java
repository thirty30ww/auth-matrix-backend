package com.thirty.user.service.facade.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogOperationVO;
import com.thirty.user.service.basic.LogOperationService;
import com.thirty.user.service.domain.Log.LogDomain;
import com.thirty.user.service.facade.LogFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogFacadeImpl implements LogFacade {
    @Resource
    private LogDomain logDomain;
    @Resource
    private LogOperationService logOperationService;

    /**
     * 获取日志操作列表
     * @param pageQueryDTO 分页查询参数
     * @return 日志操作列表
     */
    @Override
    public IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO) {
        return logDomain.getLogOperations(pageQueryDTO);
    }

    /**
     * 获取日志操作详情
     * @param id 日志操作ID
     * @return 日志操作详情
     */
    @Override
    public LogOperationVO getLogOperation(Integer id) {
        return logDomain.getLogOperation(id);
    }

    /**
     * 获取日志操作的代码列表
     * @return 代码列表
     */
    @Override
    public List<Integer> getCodes() {
        return logOperationService.getCodes();
    }

    /**
     * 获取日志操作的模块列表
     * @return 模块列表
     */
    @Override
    public List<String> getModules() {
        return logOperationService.getModules();
    }

}
