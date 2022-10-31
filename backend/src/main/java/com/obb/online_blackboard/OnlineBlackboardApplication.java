package com.obb.online_blackboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"tool", "com.obb.online_blackboard"})
@EnableScheduling
public class OnlineBlackboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBlackboardApplication.class, args);
    }

}
