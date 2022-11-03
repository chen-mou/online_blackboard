package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Line.java
 * @date 2022-11-03 15:52
 * @logs[0] 2022-11-03 15:52 陈桢梁 创建了Line.java文件
 */
@Data
@NoArgsConstructor
public class Line extends Shape {

    int endX;

    int endY;

    public Line(Map<String, Object> map){
        super(map);
        if(!map.containsKey("endX") || !map.containsKey("endY")){
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
        return new Line(obj);
    }
}
