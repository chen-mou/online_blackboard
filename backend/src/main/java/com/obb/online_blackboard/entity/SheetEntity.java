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
    private Map<Long, Deque<Operate>> useStack;

    private Map<Long, Stack<Operate>> redoStack;

    private Set<Long> shapes;


    @Transient
    private List<Shape> shapeEntities;


    public SheetEntity() {
        shapes = new HashSet<>();
        useStack = new HashMap<>();
        redoStack = new HashMap<>();
    }

    private void StackOperation(long userId, Operate op){
        if(!useStack.containsKey(userId)){
            useStack.put(userId, new LinkedList<>());
            redoStack.put(userId, new Stack<>());
        }
        Deque<Operate> list = useStack.get(userId);
        if(redoStack.containsKey(userId) && redoStack.get(userId).size() > 0){
            redoStack.clear();
        }
        list.addLast(op);
        if(list.size() > 30){
            list.pollFirst();
        }
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
        if(!useStack.containsKey(userId) || useStack.get(userId).size() == 0){
            throw new OperationException(500, "无法撤销");
        }
        Deque<Operate> deque = useStack.get(userId);
        Operate op = deque.pollLast();
        redoStack.get(userId).push(op);
        op.rollback(shapes, roomId);

    }

    public void redo(long userId){
        if(!redoStack.containsKey(userId) || redoStack.get(userId).size() == 0){
            throw new OperationException(500, "无法重做了");
        }
        Operate op = redoStack.get(userId).pop();
        useStack.get(userId).addLast(op);
        op.redo(this.shapes, this.roomId);
    }

}
