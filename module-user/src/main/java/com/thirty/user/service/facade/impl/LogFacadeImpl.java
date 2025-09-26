package com.thirty.user.service.facade.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.system.api.SettingApi;
import com.thirty.user.enums.model.RolesType;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.dto.LogOperationDTO;
import com.thirty.user.model.vo.LogLoginVO;
import com.thirty.user.model.vo.LogOperationVO;
import com.thirty.user.service.basic.LogLoginService;
import com.thirty.user.service.basic.LogOperationService;
import com.thirty.user.service.domain.Log.LogDomain;
import com.thirty.user.service.domain.role.builder.RolesBuilderFactory;
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
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private RolesBuilderFactory rolesBuilderFactory;
    @Resource
    private SettingApi settingApi;

    /**
     * 获取日志操作列表
     * @param userId 用户ID
     * @param pageQueryDTO 分页查询参数
     * @return 日志操作列表
     */
    @Override
    public IPage<LogOperationVO> getLogOperations(Integer userId, PageQueryDTO<LogOperationDTO> pageQueryDTO) {
        if (settingApi.hasPermissionDisplay()) {
            // 传入有权限查看的角色类型
            List<Integer> permittedRoleIds = rolesBuilderFactory.create(userId).forRoleTypes(RolesType.CHILD_AND_GLOBAL.toRoleTypes()).buildIds();
            return logDomain.getLogOperations(pageQueryDTO, permittedRoleIds);
        }
        return logDomain.getLogOperations(pageQueryDTO);
    }

    /**
     * 获取日志登录列表
     * @param userId 用户ID
     * @param pageQueryDTO 分页查询参数
     * @return 日志登录列表
     */
    @Override
    public IPage<LogLoginVO> getLogLogins(Integer userId, PageQueryDTO<LogLoginDTO> pageQueryDTO) {
        if (settingApi.hasPermissionDisplay()) {
            // 传入有权限查看的角色类型
            List<Integer> permittedRoleIds = rolesBuilderFactory.create(userId).forRoleTypes(RolesType.CHILD_AND_GLOBAL.toRoleTypes()).buildIds();
            return logDomain.getLogLogins(pageQueryDTO, permittedRoleIds);
        }
        return logDomain.getLogLogins(pageQueryDTO);
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
    public List<Integer> getLogOperationCodes() {
        return logOperationService.getCodes();
    }

    /**
     * 获取日志操作的模块列表
     * @return 模块列表
     */
    @Override
    public List<String> getLogOperationModules() {
        return logOperationService.getModules();
    }

     /**
      * 获取日志登录的浏览器列表
      * @return 浏览器列表
      */
     @Override
     public List<String> getLogLoginBrowsers() {
        return logLoginService.getBrowsers();
    }

     /**
      * 获取日志登录的操作系统列表
      * @return 操作系统列表
      */
     @Override
     public List<String> getLogLoginOperatingSystems() {
        return logLoginService.getOperatingSystems();
    }
}
