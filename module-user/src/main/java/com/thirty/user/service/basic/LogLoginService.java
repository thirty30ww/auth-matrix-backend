package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.dto.LogLoginDTO;
import com.thirty.user.model.entity.LogLogin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.vo.LogLoginVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【log_login(登录日志表)】的数据库操作Service
* @createDate 2025-09-25 14:40:23
*/
public interface LogLoginService extends IService<LogLogin> {

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @return 登录日志列表
     */
    IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO);

    /**
     * 获取登录日志列表
     * @param pageQueryDTO 分页查询参数
     * @param permittedRoleIds 权限角色ID列表
     * @return 登录日志列表
     */
    IPage<LogLoginVO> getLogLogins(PageQueryDTO<LogLoginDTO> pageQueryDTO, List<Integer> permittedRoleIds);

     /**
      * 获取操作系统列表
      * @return 操作系统列表
      */
    List<String> getOperatingSystems();

     /**
      * 获取浏览器列表
      * @return 浏览器列表
      */
    List<String> getBrowsers();

     /**
      * 删除登录日志
      * 会删除所有创建时间早于当前时间减去保留天数的登录日志
      * @param limitDays 保留天数
      * @return 删除的登录日志数量
      */
    Integer deleteLogLogins(Integer limitDays);
}
