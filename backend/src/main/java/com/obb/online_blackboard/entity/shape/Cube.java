package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Cube.java
 * @date 2022-10-29 11:35
 * @logs[0] 2022-10-29 11:35 陈桢梁 创建了Cube.java文件
 */
@Data
@NoArgsConstructor
public class Cube extends Shape {


    public Cube(Map<String, Object> map)  {
        super(map);
    }


    @Override
    public Object handler(Map<String, Object> obj) {
        return new Cube(obj);
    }
}
