package com.obb.online_blackboard.entity.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    protected int index;

    protected Point start;

    protected Point end;

    protected long userId;

    protected Pen pen;


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
                    ObjectMapper om = new ObjectMapper();
                    String json = om.writeValueAsString(map.get(name));
                    switch(name){
                        case "start":
                            this.start = om.readValue(json, Point.class);
                            break;
                        case "end":
                            this.end = om.readValue(json, Point.class);
                            break;
                        case "pen":
                            this.pen = om.readValue(json, Pen.class);
                            break;
                        default:
                            field.setAccessible(true);
                            field.set(this, map.get(name));
                    }
                }catch (IllegalAccessException e){
                    throw new OperationException(500, "类型无法设置");
                } catch (JsonProcessingException e) {
                    throw new OperationException(500, "json解析失败");
                }
            }
        }
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
