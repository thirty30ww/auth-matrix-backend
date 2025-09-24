package com.thirty.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.LogResultCode;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogOperationVO;
import com.thirty.user.service.facade.LogFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogFacade logFacade;

    /**
     * 获取操作日志列表
     */
    @PostMapping("/operation/list")
    public ResultDTO<IPage<LogOperationVO>> getLogOperations(@RequestBody PageQueryDTO<LogOperationDTO> pageQueryDTO) {
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_LIST_SUCCESS, logFacade.getLogOperations(pageQueryDTO));
    }

    /**
     * 获取操作日志详情
     */
    @GetMapping("/operation/get")
    public ResultDTO<LogOperationVO> getLogOperation(@RequestParam Integer id) {
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_SUCCESS, logFacade.getLogOperation(id));
    }

    /**
     * 获取日志操作的代码列表
     */
    @GetMapping("/operation/codes")
    public ResultDTO<List<Integer>> getCodes() {
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_CODE_LIST_SUCCESS, logFacade.getCodes());
    }

    /**
     * 获取日志操作的模块列表
     */
    @GetMapping("/operation/modules")
    public ResultDTO<List<String>> getModules() {
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_MODULE_LIST_SUCCESS, logFacade.getModules());
    }
}
