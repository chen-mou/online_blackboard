package com.obb.online_blackboard.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import tool.annotation.JsonKey;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc JsonMessageHandler.java
 * @date 2022-10-29 16:14
 * @logs[0] 2022-10-29 16:14 陈桢梁 创建了JsonMessageHandler.java文件
 */
public class JsonMessageHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        JsonKey j = parameter.getParameterAnnotation(JsonKey.class);
        return j != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        byte[] value = (byte[]) message.getPayload();
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = om.readValue(new String(value), HashMap.class);
        JsonKey j = parameter.getParameterAnnotation(JsonKey.class);
        String key = j.name().equals("") ? parameter.getParameterName(): j.name();
        return map.get(key);
    }
}
