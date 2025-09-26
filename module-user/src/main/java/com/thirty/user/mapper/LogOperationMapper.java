package com.thirty.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.entity.LogOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thirty.user.model.vo.LogOperationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【log_operation(用户操作日志表)】的数据库操作Mapper
* @createDate 2025-09-23 15:43:58
* @Entity com.thirty.user.model.entity.LogOperation
*/
public interface LogOperationMapper extends BaseMapper<LogOperation> {
    /**
     * 获取用户操作日志列表
     * @param page 分页参数
     * @param params 查询参数
     * @param permittedRoleIds 权限角色ID列表
     * @return 用户操作日志列表
     */
    IPage<LogOperationVO> getLogOperations(IPage<LogOperationVO> page, @Param("params") LogOperationDTO params, @Param("permittedRoleIds") List<Integer> permittedRoleIds);
}




