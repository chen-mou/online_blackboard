package com.obb.online_blackboard;

import com.obb.online_blackboard.config.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import tool.encrypt.MD5;

import java.util.Date;

@SpringBootApplication(scanBasePackages = {"tool", "com.obb.online_blackboard"})
@EnableScheduling
public class OnlineBlackboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBlackboardApplication.class, args);
    }

}
