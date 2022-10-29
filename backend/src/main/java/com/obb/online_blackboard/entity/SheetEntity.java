package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Shape;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.*;

/**
 * @author 陈桢梁
 * @desc SheetEntity.java
 * @date 2022-10-27 16:54
 * @logs[0] 2022-10-27 16:54 陈桢梁 创建了SheetEntity.java文件
 */
@Data
@RedisHash("sheet")
public class SheetEntity {

    @Id
    private long id;

    private String name;

    //操作栈,只最多保存三十个操作
    private Map<Long, ArrayList<Long>> useStack;

    private Set<Long> shapes;

    //用户撤销的指针用于回退和重做
    private Map<Long, Integer> userIndex;

    public SheetEntity() {
        shapes = new HashSet<>();
        userIndex = new HashMap<>();
        useStack = new HashMap<>();
    }

    public void addStack(long userId, long shape){
        if(!useStack.containsKey(userId)){
            userIndex.put(userId, 0);
            useStack.put(userId, new ArrayList<>());
        }
        int index = userIndex.get(userId);
        ArrayList<Long> list = useStack.get(userId);
        list.subList(index, list.size()).clear();
        userIndex.put(userId, index + 1);
        list.add(shape);
    }

}
