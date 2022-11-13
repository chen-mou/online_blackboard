package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Ellipse.java
 * @date 2022-11-13 11:50
 * @logs[0] 2022-11-13 11:50 陈桢梁 创建了Ellipse.java文件
 */
@Data
@NoArgsConstructor
public class Ellipse extends Shape {

    public Ellipse(Map<String, Object> map)  {
        super(map);
    }


    @Override
    public Object handler(Map<String, Object> obj) {
        return new Ellipse(obj);
    }

}
