package tool.util.lock;

import com.obb.online_blackboard.exception.OperationException;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author 陈桢梁
 * @desc LockUtil.java
 * @date 2022-09-24 17:02
 * @logs[0] 2022-09-24 17:02 陈桢梁 创建了LockUtil.java文件
 */
@Repository
@AllArgsConstructor
public class LockUtil<T> {

    private RedissonClient client;

    private RedisTemplate<String, Object> template;


    public T getLock(String key, String lockKey, Getter<T> getter, int timeout){
        T t = (T) template.opsForValue().get(key);
        if(t != null){
            return t;
        }
        System.out.println(lockKey);
        RLock lock = client.getLock(lockKey);
        boolean get = lock.tryLock();
        if (!get) {
            try {
                get = lock.tryLock(timeout, TimeUnit.SECONDS);
                if (!get) {
                    throw new OperationException(503, "服务器繁忙");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("中断错误");
            }
        }
        t = getter.get();
        if(t != null){
            template.opsForValue().set(key, t, 20, TimeUnit.MINUTES);
        }else{
            template.opsForValue().set(key, t, 1, TimeUnit.MINUTES);
        }
        lock.unlock();
        return t;

    }

}
