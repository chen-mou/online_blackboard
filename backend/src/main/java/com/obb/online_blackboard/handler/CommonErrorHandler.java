package com.obb.online_blackboard.handler;

import com.obb.online_blackboard.exception.OperationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tool.result.Result;

/**
 * @author 陈桢梁
 * @desc CommonErrorHandler.java
 * @date 2021-12-02 17:16
 * @logs[0] 2021-12-02 17:16 陈桢梁 创建了CommonErrorHandler.java文件
 */
@RestControllerAdvice
public class CommonErrorHandler {

    @ExceptionHandler(OperationException.class)
    public Result runtimeHandler(RuntimeException e){
        e.printStackTrace();
        return Result.Error(e);
    }

}
