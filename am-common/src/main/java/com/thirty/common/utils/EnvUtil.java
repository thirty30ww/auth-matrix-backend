package com.thirty.common.utils;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

/**
 * 环境变量工具类
 * 用于加载 .env 文件中的环境变量
 */
@Slf4j
public class EnvUtil {
    
    /**
     * 加载 .env 文件并设置为系统属性
     */
    public static void loadEnvFile() {
        // 获取当前工作目录
        String currentDir = System.getProperty("user.dir");
        log.debug("当前工作目录: {}", currentDir);
        
        // 确定.env文件的路径 - 始终指向backend目录
        String envPath = currentDir.endsWith("backend") ? "." : 
                        currentDir.endsWith("am-core") ? ".." :
                        ".";
        
        log.debug("尝试从路径加载.env文件: {}", envPath);
        
        // 加载.env文件
        Dotenv dotenv = Dotenv.configure()
                .directory(envPath)  // 使用计算出的路径
                .ignoreIfMissing()  // 如果.env文件不存在，不报错
                .load();
        
        // 检查是否加载成功
        if (dotenv.entries().isEmpty()) {
            log.warn(".env 文件不存在或为空");
        } else {
            log.info("加载了 {} 个环境变量", dotenv.entries().size());
        }
        
        // 将.env中的变量设置为系统环境变量
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}