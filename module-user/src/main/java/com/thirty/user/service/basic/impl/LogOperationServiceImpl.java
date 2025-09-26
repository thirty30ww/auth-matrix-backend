package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.entity.LogOperation;
import com.thirty.user.model.vo.LogOperationVO;
import com.thirty.user.service.basic.LogOperationService;
import com.thirty.user.mapper.LogOperationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【log_operation(用户操作日志表)】的数据库操作Service实现
* @createDate 2025-09-23 15:43:58
*/
@Service
public class LogOperationServiceImpl extends ServiceImpl<LogOperationMapper, LogOperation>
    implements LogOperationService{

    @Resource
    private LogOperationMapper logOperationMapper;

    /**
     * 获取用户操作日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 用户操作日志列表
     */
    @Override
    public IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO) {
        IPage<LogOperationVO> page = new Page<>(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());
        return logOperationMapper.getLogOperations(page, pageQueryDTO.getParams(), List.of());
    }

    /**
     * 获取用户操作日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 角色ID列表
     * @return 用户操作日志列表
     */
    @Override
    public IPage<LogOperationVO> getLogOperations(PageQueryDTO<LogOperationDTO> pageQueryDTO, List<Integer> permittedRoleIds) {
        IPage<LogOperationVO> page = new Page<>(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());
        return logOperationMapper.getLogOperations(page, pageQueryDTO.getParams(), permittedRoleIds);
    }

    /**
     * 获取日志操作的代码列表
     * @return 代码列表
     */
    @Override
    public List<Integer> getCodes() {
        List<LogOperation> logOperations = list(new QueryWrapper<LogOperation>()
                .select("DISTINCT code")
                .isNotNull("code")
                .orderByAsc("code")
        );
        return LogOperation.getCodes(logOperations);
    }


    /**
     * 获取日志操作的模块列表
     * @return 模块列表
     */
    @Override
    public List<String> getModules() {
        List<LogOperation> logOperations = list(new QueryWrapper<LogOperation>()
                .select("DISTINCT module")
                .isNotNull("module")
                .orderByAsc("module")
        );
        return LogOperation.getModules(logOperations);
    }

    /**
     * 删除用户操作日志
     * 会删除所有创建时间早于当前时间减去保留天数的用户操作日志
     * @param limitDays 保留天数
     * @return 删除的用户操作日志数量
     */
    @Override
    public Integer deleteLogOperations(Integer limitDays) {
        return logOperationMapper.delete(new QueryWrapper<LogOperation>()
                        .le("create_time", LocalDateTime.now().minusDays(limitDays)));
    }
}




