package com.teavamc.rpcdatadao;

import com.teavamc.rpcdatadao.config.redis.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
//        scanBasePackages = {"com.teavamc.rpcdatadao"},
//        exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class}
)
// 导入指定的类交给容器托管
@ImportAutoConfiguration(classes = {RedisConfig.class})
// 导入自定义的 spring 配置文件
@ImportResource(value = {"classpath*:spring/app-config.xml"})
@EnableScheduling
public class RpcDataDaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDataDaoApplication.class, args);
    }

}
