package com.obb.online_blackboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"tool", "com.obb.online_blackboard"})
public class OnlineBlackboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBlackboardApplication.class, args);
    }

}
