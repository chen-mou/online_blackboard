package com.obb.online_blackboard.entity.base;

import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import tool.ShapeFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Shape.java
 * @date 2022-10-27 09:12
 * @logs[0] 2022-10-27 09:12 陈桢梁 创建了Shape.java文件
 */
@Data
@RedisHash("Shape")
@NoArgsConstructor
public class Shape implements Special{

    @Id
    private long id;

    protected String type;

    @Indexed
    protected long sheetId;

    protected String color;

    protected int index;

    protected int x;

    protected int y;

    protected double lineWidth;

    public Shape(Shape s){
        this.type = s.type;
        this.sheetId = s.sheetId;
        this.color = s.color;
        this.index = s.index;
        this.x = s.x;
        this.y = s.y;
        this.lineWidth = s.lineWidth;
    }

    public Shape(Map<String, Object> map) {
        Class clazz = this.getClass().getSuperclass();
        if(clazz != Shape.class){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            String name = field.getName();
            if(map.containsKey(name)){
                try{
                    field.setAccessible(true);
                    field.set(this, map.get(name));
                }catch (IllegalAccessException e){
                    throw new OperationException(500, "类型无法设置");
                }
            }
        }
    }

    public void setLineWidth(double lineWidth){
        this.lineWidth = lineWidth;
    }

    public void setColor(String color){
        this.color = color;
    }

    @Override
    public Object handler(Map<String, Object> obj) {
        try {
            return ShapeFactory.getShape(obj);
        }catch (Exception e){
            throw new OperationException(500, e.getMessage());
        }
    }

    protected static void set(Shape s, Map<String, Object> map){
        Field[] fields = s.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try{
                field.set(s, map.get(field.getName()));
            }catch (IllegalAccessException e){
                throw new OperationException(500, "参数设置异常");
            }

        }
    }
}
