package com.obb.online_blackboard.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import tool.annotation.JsonKey;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 陈桢梁
 * @desc JsonKeyHandler.java
 * @date 2022-10-28 16:53
 * @logs[0] 2022-10-28 16:53 陈桢梁 创建了JsonKeyHandler.java文件
 */
public class JsonKeyHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        JsonKey j = parameter.getParameterAnnotation(JsonKey.class);
        return j != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JsonKey j = parameter.getParameterAnnotation(JsonKey.class);
        String key = j.name().equals("") ? parameter.getParameterName(): j.name();
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        Map<String, Object> m = (Map<String, Object>) req.getAttribute("value");
        if(m != null){
            return m.get(key);
        }
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        ObjectMapper om = new ObjectMapper();
        m = om.readValue(json, HashMap.class);
        req.setAttribute("value", m);
        return m.get(key);
    }
}
