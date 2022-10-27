package com.obb.online_blackboard.handler;

import com.obb.online_blackboard.entity.UserEntity;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import tool.annotation.NotNeedLogin;
import tool.annotation.UserInfo;

/**
 * @author 陈桢梁
 * @desc UserInfoHandler.java
 * @date 2022-01-29 23:25
 * @logs[0] 2022-01-29 23:25 陈桢梁 创建了UserInfoHandler.java文件
 */
public class UserInfoHandler implements HandlerMethodArgumentResolver {
    @SneakyThrows
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if(!methodParameter.hasParameterAnnotation(UserInfo.class)){
            return false;
        }
        if(!methodParameter.getParameterType().isAssignableFrom(UserEntity.class)){
            throw new Exception("修饰的类型有误");
        }
        return methodParameter.getMethod().getAnnotation(NotNeedLogin.class) == null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory){
        UserEntity user = (UserEntity) nativeWebRequest.getAttribute("CurrentUser", RequestAttributes.SCOPE_REQUEST);
        if(user == null){
            UserInfo info = methodParameter.getParameterAnnotation(UserInfo.class);
            if(info.mustValue()){
                return null;
            }
            throw new RuntimeException("未登录");
        }
        return user;
    }
}
