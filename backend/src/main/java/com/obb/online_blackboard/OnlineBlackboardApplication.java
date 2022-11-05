package com.obb.online_blackboard;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.entity.base.Special;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;


@SpringBootApplication(scanBasePackages = {"tool", "com.obb.online_blackboard"})
@EnableScheduling
@EnableAspectJAutoProxy
public class OnlineBlackboardApplication {

    private static final File file = new File("./file");

    public static void main(String[] args) {
        if(file.exists() && !file.isDirectory()){
            file.mkdir();
        }
        SpringApplication.run(OnlineBlackboardApplication.class, args);
    }

}
