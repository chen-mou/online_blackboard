package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Text.java
 * @date 2022-11-05 15:56
 * @logs[0] 2022-11-05 15:56 陈桢梁 创建了Text.java文件
 */
@Data
@NoArgsConstructor
public class Text extends Shape {

    private String main;

    private double size;

    private String layout;

    public Text(Map<String, Object> map){
        super(map);
        Shape.set(this, map);
    }

    @Override
    public Text handler(Map<String, Object> map){
        return new Text(map);
    }

}
