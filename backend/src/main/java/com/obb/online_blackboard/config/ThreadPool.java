package com.obb.online_blackboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈桢梁
 * @desc ThreadPool.java
 * @date 2022-11-07 19:28
 * @logs[0] 2022-11-07 19:28 陈桢梁 创建了ThreadPool.java文件
 */
@Component
public class ThreadPool {

    @Bean
    public ThreadPoolExecutor getPool(){
        return new ThreadPoolExecutor(4, 8, 5,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(20), new ThreadPoolExecutor.AbortPolicy());
    }

}
