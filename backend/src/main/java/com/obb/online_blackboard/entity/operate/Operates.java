package com.obb.online_blackboard.entity.operate;

import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc Operates.java
 * @date 2022-11-05 13:46
 * @logs[0] 2022-11-05 13:46 陈桢梁 创建了Operates.java文件
 */
@Data
@RedisHash("${operates}")
public class Operates {

    @Id
    long id;

    List<Operate> redo;

    List<Operate> operates;

    public Operates(){
        redo = new LinkedList<>();
        operates = new LinkedList<>();
    }

    public Operates(long sheetId){
        this();
        this.id = sheetId;
    }

    public void addOperates(Operate op){
        redo.clear();
        operates.add(op);
        if(operates.size() > 30){
            operates.remove(0);
        }
    }

    private Operate base(List<Operate> add, List<Operate> del){
        if(del.size() == 0){
            throw new OperationException(500, "操作有误");
        }
        Operate op = del.remove(del.size() - 1);
        add.add(op);
        return op;
    }

    public Operate rollback(){
        return base(redo, operates);
    }

    public Operate redo(){
        return base(operates, redo);
    }

}
