package com.thirty.core;

import io.github.cdimascio.dotenv.Dotenv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.thirty.core",
        "com.thirty.user",
        "com.thirty.common"
})
@MapperScan({
        "com.thirty.user.mapper"
})
public class ModuleCoreApplication {
    public static void main(String[] args) {
        // 加载.env文件
        Dotenv dotenv = Dotenv.configure()
                .directory(".")  // 在项目根目录查找.env文件
                .ignoreIfMissing()  // 如果.env文件不存在，不报错
                .load();
        
        // 将.env中的变量设置为系统环境变量
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        
        SpringApplication.run(ModuleCoreApplication.class, args);
    }
}
