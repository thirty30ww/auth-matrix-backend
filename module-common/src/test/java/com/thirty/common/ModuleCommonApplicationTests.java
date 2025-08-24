package com.thirty.common;

import com.thirty.common.api.SettingApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModuleCommonApplicationTests {

    @Resource
    private SettingApi settingApi;

    @Test
    void contextLoads() {
        boolean isPermissionDisplay = settingApi.hasPermissionDisplay();
        System.out.println(isPermissionDisplay);
    }

}
