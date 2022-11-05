package com.obb.online_blackboard.task;

import com.obb.online_blackboard.dao.mysql.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc IdTask.java
 * @date 2022-11-05 17:02
 * @logs[0] 2022-11-05 17:02 陈桢梁 创建了IdTask.java文件
 */
@Component
public class IdTask {

    @Resource
    IdGenerator id;

    @Resource
    RedisTemplate<String, Object> redis;

    @Value("${spring.application.name}")
    String name;

    @Scheduled(cron = "* * */1 * * *")
    public void updateKey(){
        List<String> names = id.getAllName();
        names.forEach(item -> {
            String key = name + "-" + item + "-id";
            Object obj = redis.opsForValue().get(key);
            if(obj == null){
                return;
            }
            id.updateId(item, (Integer)obj);
        });
    }
}
