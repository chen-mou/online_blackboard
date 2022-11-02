package tool;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc ShapeFactory.java
 * @date 2022-11-02 17:02
 * @logs[0] 2022-11-02 17:02 陈桢梁 创建了ShapeFactory.java文件
 */
public class ShapeFactory {

    public static Shape getShape(Map<String, Object> map) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String type = (String) map.get("type");
        if (type == null) {
            throw new OperationException(500, "缺少参数type");
        }
        Class clazz;
        try {
            clazz = Class.forName("com.obb.online_blackboard.entity.shape." + type);
        }catch (ClassNotFoundException e){
            throw new OperationException(500, "type类型有误");
        }
        Constructor constructor  = clazz.getDeclaredConstructor(Map.class);
        return (Shape) constructor.newInstance(map);
    }

}
