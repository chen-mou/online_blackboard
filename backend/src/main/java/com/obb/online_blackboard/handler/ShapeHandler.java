package com.obb.online_blackboard.handler;

import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }
}
