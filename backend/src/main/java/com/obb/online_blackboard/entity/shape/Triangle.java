package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Triangle.java
 * @date 2022-11-05 15:56
 * @logs[0] 2022-11-05 15:56 陈桢梁 创建了Triangle.java文件
 */
@Data
@NoArgsConstructor
public class Triangle extends Shape {

    public Triangle(Map<String,Object> map){
        super(map);
    }

    @Override
    public Object handler(Map<String, Object> obj) {
        return new Triangle(obj);
    }
}
