package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.entity.LogOperation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.vo.LogOperationVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【log_operation(用户操作日志表)】的数据库操作Service
* @createDate 2025-09-23 15:43:58
*/
public interface LogOperationService extends IService<LogOperation> {
    /**
     * 获取用户操作日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 用户操作日志列表
     */
    IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO);

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
