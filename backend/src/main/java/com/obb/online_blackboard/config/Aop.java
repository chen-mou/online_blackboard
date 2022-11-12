package com.obb.online_blackboard.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;
import tool.annotation.Lock;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈桢梁
 * @desc Aop.java
 * @date 2022-11-01 20:55
 * @logs[0] 2022-11-01 20:55 陈桢梁 创建了Aop.java文件
 */
@Component
@Aspect
public class Aop {

    @Resource
    RedissonClient redissonClient;

    @Around("execution(* com.obb.online_blackboard.service.SheetService.*(..))")
    public Object aroundLock(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        Method method = pjp.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getMethod().getParameterTypes());
        Lock lock = method.getAnnotation(Lock.class);
        if (lock == null) {
            return pjp.proceed(args);
        } else {
            String key = lock.key();
            String argName = lock.argName();
            Parameter[] pt = method.getParameters();
            for (int i = 0; i < pt.length; i++) {
                Parameter arg = pt[i];
                if (arg.getName().equals(argName)) {
                    key = key + args[i];
                    break;
                }
            }
            RLock rl = redissonClient.getLock(key);
            rl.lock(lock.time(), TimeUnit.SECONDS);
            Object res;
            res = pjp.proceed();
            rl.unlock();
            return res;
        }

    }
}
