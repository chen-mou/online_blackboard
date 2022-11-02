package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.entity.operate.Add;
import com.obb.online_blackboard.entity.operate.Delete;
import com.obb.online_blackboard.entity.operate.Modify;
import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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

    private String roomId;

    //操作栈,只最多保存三十个操作
    private Map<Long, ArrayList<Operate>> useStack;

    private Set<Long> shapes;


    @Transient
    private List<Shape> shapeEntities;

    //用户撤销的指针用于回退和重做
    private Map<Long, Integer> userIndex;

    public SheetEntity() {
        shapes = new HashSet<>();
        userIndex = new HashMap<>();
        useStack = new HashMap<>();
    }

    private void StackOperation(long userId, Operate op){
        if(!useStack.containsKey(userId)){
            userIndex.put(userId, 0);
            useStack.put(userId, new ArrayList<>());
        }
        int index = userIndex.get(userId);
        ArrayList<Operate> list = useStack.get(userId);
        if(index < list.size()){
            list.subList(index, list.size()).clear();
        }
        if(list.size() > 30){
            list.subList(0, list.size() - 30).clear();
        }
        userIndex.put(userId, index + 1);
        list.add(op);
    }

    public void addStack(long userId, long shape){
        shapes.add(shape);
        StackOperation(userId, new Add(shape));
    }

    public void delStack(long userId, long shape){
       shapes.remove(shape);
        StackOperation(userId, new Delete(shape));
    }

    public void modStack(long userId, long from, long to){
        shapes.remove(from);
        shapes.add(to);
        StackOperation(userId, new Modify(from, to));
    }

    public void rollback(long userId){
        Integer index = userIndex.get(userId);
        if(index == null){
            useStack.put(userId, new ArrayList<>());
            userIndex.put(userId, 0);
        }
        if(index == 0){
            throw new OperationException(500, "无法撤销了");
        }
        Operate op = useStack.get(userId).get(index);
        userIndex.put(userId, index - 1);
        op.rollback(this.shapes, this.roomId);
    }

    public void redo(long userId){
        Integer index = userIndex.get(userId);
        if(index == null){
            useStack.put(userId, new ArrayList<>());
            userIndex.put(userId, 0);
        }
        if(index == useStack.get(userId).size()){
            throw new OperationException(500, "无法重做了");
        }
        Operate op = useStack.get(userId).get(index + 1);
        userIndex.put(userId, index + 1);
        op.redo(this.shapes, this.roomId);
    }

}
