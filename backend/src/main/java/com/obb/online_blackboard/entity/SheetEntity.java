package com.obb.online_blackboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.*;

/**
 * @author 陈桢梁
 * @desc SheetEntity.java
 * @date 2022-10-27 16:54
 * @logs[0] 2022-10-27 16:54 陈桢梁 创建了SheetEntity.java文件
 */
@Data
@RedisHash("${sheet}")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SheetEntity {

    @Id
    private final long id;

    private String name;

    @Indexed
    private final long roomId;

    //操作栈,只最多保存三十个操作



    @Transient
    private List<Shape> shapeEntities;


    public SheetEntity(long id, long roomId) {
        this.id = id;
        this.roomId = roomId;
    }

    private void StackOperation(Operate op){
        OperateModel operateModel = Context.getContext().getBean(OperateModel.class);
        Operates ops = operateModel.getById(this.id);
        if(ops == null){
            ops = new Operates(this.id);
        }
        ops.addOperates(op);
        operateModel.save(ops);
    }

    public void addStack(long shape){
        StackOperation(new Add(shape));
    }

    public void delStack(long shape){
        StackOperation(new Delete(shape));
    }

    public void modStack(long from, long to){
        StackOperation(new Modify(from, to));
    }

    public void rollback(){
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
        op.rollback(this.id,roomId);
        operateModel.save(ops);
    }

    public void redo(){
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
        op.redo(this.id,roomId);
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
