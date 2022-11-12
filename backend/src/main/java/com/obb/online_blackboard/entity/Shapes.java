package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 陈桢梁
 * @desc Shapes.java
 * @date 2022-11-13 01:42
 * @logs[0] 2022-11-13 01:42 陈桢梁 创建了Shapes.java文件
 */
@RedisHash("${shapes}")
@AllArgsConstructor
@Data
public class Shapes<T> {

    @Id
    private long sheetId;

    private Set<T> shapes;

    public Shapes(){
        this.shapes = new HashSet<>();
    }

    public void add(T obj){
        this.shapes.add(obj);
    }

    public void remove(T id){}

}
