package com.thirty.core;

import com.thirty.common.utils.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ApplicationTests {

    // 静态初始化块，在类加载时就执行，确保在Spring上下文初始化之前加载环境变量
    static {
        EnvUtil.loadEnvFile();
    }

    @Test
    void contextLoads() {
        log.info("测试");
    }

}
