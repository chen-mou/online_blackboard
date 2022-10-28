package tool.util.id;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 陈桢梁
 * @desc Id.java
 * @date 2022-10-27 14:39
 * @logs[0] 2022-10-27 14:39 陈桢梁 创建了Id.java文件
 */
@Component
public class Id {

    @Resource
    RedisTemplate<String, Object> redis;

    @Value("{spring.application.name}")
    String name;

    public int getId(String name){
        String key = this.name + "-" + name + "-id";
        Long id = redis.opsForValue().increment(key, 12);
        if(id == null){
            id = Long.valueOf(1000000);
            redis.opsForValue().set(key, id);
        }
        return id.intValue();
    }

}
