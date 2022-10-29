package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import lombok.Data;

/**
 * @author 陈桢梁
 * @desc Cube.java
 * @date 2022-10-29 11:35
 * @logs[0] 2022-10-29 11:35 陈桢梁 创建了Cube.java文件
 */
@Data
public class Cube extends Shape {

    private int width;

    private int height;

    private int lineWidth;

    public Cube(Shape shape){
        super(shape);
        if(shape.getType().equals("Cube")){
            Cube t = (Cube)shape;
            this.width = t.width;
            this.height = t.height;
        }
    }

    @Override
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void setColor(String color) {

    }


}
