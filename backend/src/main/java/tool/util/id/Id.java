package tool.util.id;


import com.obb.online_blackboard.dao.mysql.IdGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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

    @Resource
    RedissonClient client;

    IdGenerator key;

    @Value("${spring.application.name}")
    String name;

    @Autowired()
    public Id(IdGenerator key){
        this.key = key;
    }

    public long getId(String name){
        String key = this.name + "-" + name + "-id";
        Long id = Long.valueOf((Integer)redis.opsForValue().get(key));
        if(id == null){
            RLock lock = client.getLock(name);
            lock.lock(3, TimeUnit.SECONDS);
            id = this.key.getId(name) + 12;
            redis.opsForValue().set(key, id);
            lock.unlock();
        }
        return id;
    }

}
