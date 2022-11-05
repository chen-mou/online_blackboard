package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.entity.base.Save;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.entity.operate.Add;
import com.obb.online_blackboard.entity.operate.Delete;
import com.obb.online_blackboard.entity.operate.Modify;
import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.entity.operate.Operates;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.OperateModel;
import lombok.Data;
import org.springframework.context.ApplicationContext;
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

    private Set<Long> shapes;


    @Transient
    private List<Shape> shapeEntities;


    public SheetEntity() {
        shapes = new HashSet<>();
    }

    private void StackOperation(long userId, Operate op){
        OperateModel operateModel = Context.getContext().getBean(OperateModel.class);
        Operates ops = operateModel.getById(this.id);
        if(ops == null){
            ops = new Operates(userId, this.id);
        }
        ops.addOperates(op);
        operateModel.save(ops);
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

    public void rollback(Save save){
        OperateModel operateModel = Context.getContext().getBean(OperateModel.class);
        Operates ops = operateModel.getById(this.id);
        Operate op;
        try {
            if (ops == null) {
                throw new OperationException(500, "无法撤销了");
            }
            op = ops.rollback();
        }catch (OperationException e){
            throw new OperationException(500, "无法撤销了");
        }
        op.rollback(shapes, roomId, save);
        operateModel.save(ops);
    }

    public void redo(Save save){
        OperateModel operateModel = Context.getContext().getBean(OperateModel.class);
        Operates ops = operateModel.getById(this.id);
        Operate op;
        try {
            if (ops == null) {
                throw new OperationException(500, "无法重做了");
            }
            op = ops.redo();
        }catch (OperationException e){
            throw new OperationException(500, "无法重做了");
        }
        op.redo(shapes, roomId, save);
        operateModel.save(ops);
    }

//    public Map<Long, ArrayList<Long>> getUseStack() {
//        Map<Long, ArrayList<Long>> res = new HashMap<>();
//        useStack.forEach((key, value) -> {
//            res.put(key, new ArrayList<>(value));
//        });
//        return res;
//    }
//
//    public Map<Long, ArrayList<Long>> getRedoStack(){
//        Map<Long, ArrayList<Long>> res = new HashMap<>();
//        redoStack.forEach((key, value) -> {
//            res.put(key, new ArrayList<>(value));
//        });
//        return res;
//    }

}
