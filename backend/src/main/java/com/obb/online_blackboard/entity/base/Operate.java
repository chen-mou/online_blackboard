package com.obb.online_blackboard.entity.base;

import java.util.Set;

/**
 * @author 陈桢梁
 * @desc Operate.java
 * @date 2022-11-01 19:58
 * @logs[0] 2022-11-01 19:58 陈桢梁 创建了Operate.java文件
 */
public interface Operate {

    void rollback(Set<Long> shapes, String roomId);

    void redo(Set<Long> shapes, String roomId);

}
