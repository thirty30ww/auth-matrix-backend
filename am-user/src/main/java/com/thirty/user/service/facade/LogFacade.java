package com.thirty.user.service.facade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogLoginVO;
import com.thirty.user.model.vo.LogOperationVO;

import java.util.List;

public interface LogFacade {
    /**
     * 获取日志操作列表
     * @param userId 用户ID
     * @param pageQueryDTO 分页查询参数
     * @return 日志操作列表
     */
    IPage<LogOperationVO> getLogOperations(Integer userId, PageQueryDTO<LogOperationDTO> pageQueryDTO);

    /**
     * 获取日志登录列表
     * @param userId 用户ID
     * @param pageQueryDTO 分页查询参数
     * @return 日志登录列表
     */
    IPage<LogLoginVO> getLogLogins(Integer userId, PageQueryDTO<LogLoginDTO> pageQueryDTO);

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
    List<Integer> getLogOperationCodes();

    /**
     * 获取日志操作的模块列表
     * @return 模块列表
     */
    List<String> getLogOperationModules();

     /**
      * 获取日志登录的浏览器列表
      * @return 浏览器列表
      */
    List<String> getLogLoginBrowsers();

     /**
      * 获取日志登录的操作系统列表
      * @return 操作系统列表
      */
    List<String> getLogLoginOperatingSystems();
}
