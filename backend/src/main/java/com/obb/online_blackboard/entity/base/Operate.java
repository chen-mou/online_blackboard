package com.obb.online_blackboard.entity.base;

import com.obb.online_blackboard.config.Context;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

/**
 * @author 陈桢梁
 * @desc Operate.java
 * @date 2022-11-01 19:58
 * @logs[0] 2022-11-01 19:58 陈桢梁 创建了Operate.java文件
 */
@RedisHash("operate")
@Data
public abstract class Operate {

    @Id
    private long id;

    public abstract void rollback(Set<Long> shapes, String roomId);

    public abstract void redo(Set<Long> shapes, String roomId);




}
