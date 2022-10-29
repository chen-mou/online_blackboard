package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;

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
}
