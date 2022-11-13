package com.obb.online_blackboard.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;
import tool.encrypt.AES;

/**
 * @author 陈桢梁
 * @desc DataSource.java
 * @date 2022-10-27 10:37
 * @logs[0] 2022-10-27 10:37 陈桢梁 创建了DataSource.java文件
 */
@Component
public class DataSourcePostProcessor implements BeanPostProcessor {


    public static String DB_PREFIX ="spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties";

    public static String REDIS_PREFIX = "spring.redis-org.springframework.boot.autoconfigure.data.redis.RedisProperties";

    private final String password = "afe6d8973ced66f813a41692a53a99942bee81a49b294a50187cab80d24d3fa72bee81a49b294a50187cab80d24d3fa72bee81a49b294a50187cab80d24d3fa72bee81a49b294a50187cab80d24d3fa7";

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println(beanName);
        if(beanName.equals(DB_PREFIX)){
            DataSourceProperties dataSourceProperties=(DataSourceProperties)bean;
            dataSourceProperties.setUsername(AES.decrypt(dataSourceProperties.getUsername()));
            dataSourceProperties.setPassword(AES.decrypt(dataSourceProperties.getPassword()));
        }
//        if(beanName.equals(REDIS_PREFIX)){
//            RedisProperties redisProperties = (RedisProperties) bean;
//            redisProperties.setPassword(password);
//        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
