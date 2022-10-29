package com.obb.online_blackboard.handler;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

/**
 * @author 陈桢梁
 * @desc ShapeHandler.java
 * @date 2022-10-27 09:24
 * @logs[0] 2022-10-27 09:24 陈桢梁 创建了ShapeHandler.java文件
 */
public class ShapeHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class clazz = parameter.getParameterType();
        return Shape.class.isAssignableFrom(clazz);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        Object payload = message.getPayload();
        if (payload instanceof Shape) {
            Shape shape = (Shape) payload;
            try {
                Class clazz = Class.forName("com.obb.online_blackboard.entity.shape." + shape.getType());
                return clazz.getDeclaredConstructor(Shape.class).newInstance(shape);
            }catch (ClassNotFoundException e){
                throw new OperationException(500, "类型参数有误");
            }
        }
        return null;
    }

}
