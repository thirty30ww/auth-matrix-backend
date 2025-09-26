package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.entity.LogLogin;
import com.thirty.user.mapper.LogLoginMapper;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.LogLoginVO;
import com.thirty.user.service.basic.LogLoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【log_login(登录日志表)】的数据库操作Service实现
* @createDate 2025-09-25 14:40:23
*/
@Service
public class LogLoginServiceImpl extends ServiceImpl<LogLoginMapper, LogLogin>
    implements LogLoginService {

    @Resource
    private LogLoginMapper logLoginMapper;

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 登录日志列表
     */
    @Override
    public IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO) {
        IPage<LogLoginVO> page = new Page<>(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());
        return logLoginMapper.getLogLogins(page, pageQueryDTO.getParams(), List.of());
    }

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 权限角色ID列表
     * @return 登录日志列表
     */
    @Override
    public IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO, List<Integer> permittedRoleIds) {
        IPage<LogLoginVO> page = new Page<>(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());
        return logLoginMapper.getLogLogins(page, pageQueryDTO.getParams(), permittedRoleIds);
    }

    /**
     * 获取操作系统列表
     * @return 操作系统列表
     */
    @Override
    public List<String> getOperatingSystems() {
        List<LogLogin> logLogins = list(new QueryWrapper<LogLogin>()
                .select("DISTINCT operating_system")
                .isNotNull("operating_system")
                .orderByAsc("operating_system")
        );
        return LogLogin.getOperatingSystems(logLogins);
    }

    /**
     * 获取浏览器列表
     * @return 浏览器列表
     */
    @Override
    public List<String> getBrowsers() {
        List<LogLogin> logLogins = list(new QueryWrapper<LogLogin>()
                .select("DISTINCT browser")
                .isNotNull("browser")
                .orderByAsc("browser")
        );
        return LogLogin.getBrowsers(logLogins);
    }

     /**
      * 删除登录日志
      * 会删除所有创建时间早于当前时间减去保留天数的登录日志
      * @param limitDays 保留天数
      * @return 删除的登录日志数量
      */
     @Override
     public Integer deleteLogLogins(Integer limitDays) {
         return logLoginMapper.delete(new QueryWrapper<LogLogin>()
                         .le("create_time", LocalDateTime.now().minusDays(limitDays)));
    }
}




