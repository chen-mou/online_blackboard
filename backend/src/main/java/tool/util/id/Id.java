package tool.util.id;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.dao.mysql.IdGenerator;
import com.obb.online_blackboard.exception.OperationException;
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
    RedissonClient redissonClient;

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
        Object val = redis.opsForValue().get(key);
        long id;
        if(val == null){
            RLock lock = redissonClient.getLock(name + "_id_lock");
            boolean tr = lock.tryLock();
            if(!tr) {
                try {
                    tr = lock.tryLock(3, TimeUnit.SECONDS);
                    if(!tr){
                        throw new OperationException(500, "服务器繁忙");
                    }
                }catch (InterruptedException e){
                    throw new OperationException(500, "中断错误" + e.getMessage());
                }
            }else{
                long dbId = this.key.getId(name);
                id = Math.toIntExact(dbId + 12);
//                this.key.updateId(name, id);
                redis.opsForValue().set(key, id);
            }
            lock.unlock();
        }
        id = redis.opsForValue().increment(key, 12);
        return id;
    }

}
