package com.thirty.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    
    /**
     * 允许无认证访问的路径列表
     */
    private List<String> permitAllPaths = new ArrayList<>();
    
    /**
     * 默认构造函数，设置默认的允许访问路径
     */
    public SecurityProperties() {
        // 设置默认的允许访问路径
        permitAllPaths.add("/auth/**");
        permitAllPaths.add("/setting/public/**");
    }
}