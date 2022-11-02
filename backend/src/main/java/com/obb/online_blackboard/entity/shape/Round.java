package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;

import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Round.java
 * @date 2022-10-29 16:23
 * @logs[0] 2022-10-29 16:23 陈桢梁 创建了Round.java文件
 */
public class Round extends Shape {

    private double radius;

    public Round(Shape s){
        super(s);
        Round r = (Round) s;
        this.radius = r.radius;
    }

    public Round(Map<String, Object> map){
        super(map);
        if(!map.containsKey(radius)){
            throw new OperationException(500, "缺少参数radius");
        }
        radius = (double) map.get("radius");
    }

    @Override
    public Object handler(Map<String, Object> obj) {
        return new Round(obj);
    }
}
