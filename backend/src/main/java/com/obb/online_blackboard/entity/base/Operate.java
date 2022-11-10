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
public interface Operate {

    void rollback(Set<Long> shapes, long sheetId,long roomId, Save save);

    void redo(Set<Long> shapes, long sheetId, long roomId, Save save);




}
