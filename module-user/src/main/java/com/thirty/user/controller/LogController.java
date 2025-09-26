package com.thirty.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.LogResultCode;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogLoginVO;
import com.thirty.user.model.vo.LogOperationVO;
import com.thirty.user.service.facade.LogFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogFacade logFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取操作日志列表
     */
    @PostMapping("/operation/list")
    public ResultDTO<IPage<LogOperationVO>> getLogOperations(@RequestBody PageQueryDTO<LogOperationDTO> pageQueryDTO, @RequestHeader("Authorization") String authHeader) {
        // 从请求头中获取用户ID
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_LIST_SUCCESS, logFacade.getLogOperations(userId, pageQueryDTO));
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
    public ResultDTO<List<Integer>> getLogOperationCodes() {
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_CODE_LIST_SUCCESS, logFacade.getLogOperationCodes());
    }

    /**
     * 获取日志操作的模块列表
     */
    @GetMapping("/operation/modules")
    public ResultDTO<List<String>> getLogOperationModules() {
        return ResultDTO.of(LogResultCode.GET_OPERATE_LOG_MODULE_LIST_SUCCESS, logFacade.getLogOperationModules());
    }

    /**
     * 获取登录日志列表
     */
    @PostMapping("/login/list")
    public ResultDTO<IPage<LogLoginVO>> getLogLogins(@RequestBody PageQueryDTO<LogLoginDTO> pageQueryDTO, @RequestHeader("Authorization") String authHeader) {
        // 从请求头中获取用户ID
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        return ResultDTO.of(LogResultCode.GET_LOGIN_LOG_LIST_SUCCESS, logFacade.getLogLogins(userId, pageQueryDTO));
    }

    /**
     * 获取日志登录的浏览器列表
     */
    @GetMapping("/login/browsers")
    public ResultDTO<List<String>> getLogLoginBrowsers() {
        return ResultDTO.of(LogResultCode.GET_LOGIN_LOG_BROWSER_LIST_SUCCESS, logFacade.getLogLoginBrowsers());
    }

    /**
     * 获取日志登录的操作系统列表
     */
    @GetMapping("/login/operating-systems")
    public ResultDTO<List<String>> getLogLoginOperatingSystems() {
        return ResultDTO.of(LogResultCode.GET_LOGIN_LOG_OPERATING_SYSTEM_LIST_SUCCESS, logFacade.getLogLoginOperatingSystems());
    }
}
