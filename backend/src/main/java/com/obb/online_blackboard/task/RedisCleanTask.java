package com.obb.online_blackboard.task;

import com.obb.online_blackboard.model.ShapeModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 陈桢梁
 * @desc RedisCleanTask.java
 * @date 2022-11-06 16:47
 * @logs[0] 2022-11-06 16:47 陈桢梁 创建了RedisCleanTask.java文件
 */
@Component
public class RedisCleanTask {


    @Resource
    ShapeModel shapeModel;

    @Scheduled(cron = "* */1 * * * *")
    public void clean(){

    }


}
