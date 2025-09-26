package com.thirty.core;

import com.thirty.user.service.task.LogClearTask;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ApplicationTests {

    @Resource
    private LogClearTask logClearTask;

    @Test
    void testLogClearTask() {
        logClearTask.clearLogOperation();
    }


}
