package com.thirty.user.service.task;

import com.thirty.user.service.basic.LogLoginService;
import com.thirty.user.service.basic.LogOperationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 * @description 日志清除任务
 * @createDate 2025-09-23 15:43:58
 */
@Slf4j
@Service
public class LogClearTask {

    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LogOperationService logOperationService;

    /**
     * 清除登录日志
     * 会删除所有创建时间早于当前时间减去保留天数的登录日志
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearLogLogin() {
        Integer number = logLoginService.deleteLogLogins(7);
        
        log.info("删除登录日志 {} 条", number);
    }

    /**
     * 清除操作日志
     * 会删除所有创建时间早于当前时间减去保留天数的操作日志
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearLogOperation() {
        Integer number = logOperationService.deleteLogOperations(7);

        log.info("删除操作日志 {} 条", number);
    }
}
