package com.obb.online_blackboard.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.entity.base.Special;
import com.obb.online_blackboard.exception.OperationException;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import tool.annotation.JsonKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc JsonMessageHandler.java
 * @date 2022-10-29 16:14
 * @logs[0] 2022-10-29 16:14 陈桢梁 创建了JsonMessageHandler.java文件
 */
public class JsonMessageHandler implements HandlerMethodArgumentResolver {

    private Method specialHandle(MethodParameter parameter){
        Class clazz = parameter.getParameterType();
        if(Special.class.isAssignableFrom(clazz)) {
            try {
                return clazz.getMethod("handler", Map.class);
            } catch (NoSuchMethodException e){
                throw new OperationException(500, "不太可能的报错");
            }
        }
        return null;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        JsonKey j = parameter.getParameterAnnotation(JsonKey.class);
        return j != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        byte[] value = (byte[]) message.getPayload();
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map;

        map = om.readValue(new String(value), HashMap.class);

        JsonKey j = parameter.getParameterAnnotation(JsonKey.class);
        String key = j.name().equals("") ? parameter.getParameterName(): j.name();
        Method m = specialHandle(parameter);
        if(m != null){
            Constructor constructor = parameter.getParameterType().getConstructor();
            constructor.setAccessible(true);
            Object obj = constructor.newInstance();
            return m.invoke(obj, map.get(key));
        }
        return map.get(key);
    }
}
