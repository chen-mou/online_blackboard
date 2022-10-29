package com.obb.online_blackboard.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.entity.shape.Cube;
import com.obb.online_blackboard.exception.OperationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        ObjectMapper om = new ObjectMapper();
        Shape s = om.readValue(json.getBytes(StandardCharsets.UTF_8), Shape.class);
        switch(s.getType()){
            case "Cube":
                return new Cube(s);
        }
        throw new OperationException(500, "绘图类型有误");
    }
}
