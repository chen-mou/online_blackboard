package com.obb.online_blackboard.config;

import com.obb.online_blackboard.handler.JsonKeyHandler;
import com.obb.online_blackboard.handler.ShapeHandler;
import com.obb.online_blackboard.handler.UserInfoHandler;
import com.obb.online_blackboard.interceptor.CrossInterceptor;
import com.obb.online_blackboard.interceptor.LoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author 陈桢梁
 * @desc Interceptor.java
 * @date 2021-12-02 17:02
 * @logs[0] 2021-12-02 17:02 陈桢梁 创建了Interceptor.java文件
 */
@Component
public class Interceptor implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/connect/info", "/error");
        registry.addInterceptor(new CrossInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserInfoHandler());
        resolvers.add(new JsonKeyHandler());
    }
}
