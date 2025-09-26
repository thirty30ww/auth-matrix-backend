package com.thirty.user.service.domain.Log.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.converter.LogOperationConverter;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.entity.LogOperation;
import com.thirty.user.model.vo.LogLoginVO;
import com.thirty.user.model.vo.LogOperationVO;
import com.thirty.user.service.basic.DetailService;
import com.thirty.user.service.basic.LogLoginService;
import com.thirty.user.service.basic.LogOperationService;
import com.thirty.user.service.domain.Log.LogDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogDomainImpl implements LogDomain {
    @Resource
    private LogOperationService logOperationService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private DetailService detailService;

    /**
     * 获取日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 日志列表
     */
    @Override
    public IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO) {
        return logOperationService.getLogOperations(pageQueryDTO);
    }

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 登录日志列表
     */
    @Override
    public IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO) {
        return logLoginService.getLogLogins(pageQueryDTO);
    }

    /**
     * 获取日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 角色ID列表
     * @return 日志列表
     */
    @Override
    public IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO, List<Integer> permittedRoleIds) {
        return logOperationService.getLogOperations(pageQueryDTO, permittedRoleIds);
    }

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 角色ID列表
     * @return 登录日志列表
     */
    @Override
    public IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO, List<Integer> permittedRoleIds) {
        return logLoginService.getLogLogins(pageQueryDTO, permittedRoleIds);
    }



    /**
     * 获取日志详情
     * @param id 操作日志ID
     * @return 操作日志详情
     */
    @Override
    public LogOperationVO getLogOperation(Integer id) {
        LogOperation logOperation = logOperationService.getById(id);
        Integer userId = logOperation.getUserId();
        // 获取用户名称
        String name = null;
        if (userId != null) {
            name = detailService.getById(userId).getName();
        }
        return LogOperationConverter.INSTANCE.toLogOperationVO(logOperation, name);
    }
}
