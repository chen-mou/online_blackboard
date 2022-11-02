package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Cube.java
 * @date 2022-10-29 11:35
 * @logs[0] 2022-10-29 11:35 陈桢梁 创建了Cube.java文件
 */
@Data
public class Cube extends Shape {

    private double width;

    private double height;

    public Cube(Shape shape){
        super(shape);
        if(shape.getType().equals("Cube")){
            Cube t = (Cube)shape;
            this.width = t.width;
            this.height = t.height;
        }
    }

    public Cube(Map<String, Object> map)  {
        super(map);
        if(!map.containsKey("width") || !map.containsKey("height")){
            throw new OperationException(500, "缺少参数");
        }
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try{
                field.set(this, map.get(field.getName()));
            }catch (IllegalAccessException e){
                throw new OperationException(500, "参数设置异常");
            }

        }
    }


    @Override
    public Object handler(Map<String, Object> obj) {
        return new Cube(obj);
    }
}
