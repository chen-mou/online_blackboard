package com.obb.online_blackboard.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 陈桢梁
 * @desc Context.java
 * @date 2022-01-04 21:20
 * @logs[0] 2022-01-04 21:20 陈桢梁 创建了Context.java文件
 */
@Component
public class Context implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getContext(){
        return Context.context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Context.context = applicationContext;
    }
}
