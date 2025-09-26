package com.thirty.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.entity.LogLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thirty.user.model.vo.LogLoginVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【log_login(登录日志表)】的数据库操作Mapper
* @createDate 2025-09-25 14:40:23
* @Entity com.thirty.user.model.entity.LogLogin
*/
public interface LogLoginMapper extends BaseMapper<LogLogin> {
    /**
     * 获取登录日志列表
     * @param page 分页参数
     * @param params 查询参数
     * @param permittedRoleIds 权限角色ID列表
     * @return 登录日志列表
     */
    IPage<LogLoginVO> getLogLogins(IPage<LogLoginVO> page, @Param("params") LogLoginDTO params, @Param("permittedRoleIds") List<Integer> permittedRoleIds);

}




