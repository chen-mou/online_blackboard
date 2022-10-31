package tool.util.id;


import com.obb.online_blackboard.dao.mysql.IdGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tool.util.lock.LockUtil;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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
    LockUtil<Integer> lock;

    IdGenerator key;

    @Value("${spring.application.name}")
    String name;

    @Autowired()
    public Id(IdGenerator key){
        this.key = key;
    }

//    private final long MIN_ID = 1000000;

    public long getId(String name){
        String key = this.name + "-" + name + "-id";
        Integer id = lock.getLock(key, name + "id_lock",
                () -> {
            long i = this.key.getId(name) + 12;
            this.key.updateId(name, i);
            return Math.toIntExact(i);
                }, 3);
        return id;
    }

}
