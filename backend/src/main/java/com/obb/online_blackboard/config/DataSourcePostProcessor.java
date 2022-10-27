package com.obb.online_blackboard.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;
import tool.encrypt.AES;

import javax.sql.DataSource;

/**
 * @author 陈桢梁
 * @desc DataSource.java
 * @date 2022-10-27 10:37
 * @logs[0] 2022-10-27 10:37 陈桢梁 创建了DataSource.java文件
 */
@Component
public class DataSourcePostProcessor implements BeanPostProcessor {


    public static String DB_PREFIX ="spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties";

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals(DB_PREFIX)){
            DataSourceProperties dataSourceProperties=(DataSourceProperties)bean;
            dataSourceProperties.setUsername(AES.decrypt(dataSourceProperties.getUsername()));
            dataSourceProperties.setPassword(AES.decrypt(dataSourceProperties.getPassword()));
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
