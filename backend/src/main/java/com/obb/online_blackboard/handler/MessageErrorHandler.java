package com.obb.online_blackboard.handler;

import com.obb.online_blackboard.exception.OperationException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tool.result.Result;

/**
 * @author 陈桢梁
 * @desc MessageErrorHandler.java
 * @date 2022-10-30 16:55
 * @logs[0] 2022-10-30 16:55 陈桢梁 创建了MessageErrorHandler.java文件
 */

@RestControllerAdvice
public class MessageErrorHandler {

    @MessageExceptionHandler()
    @SendToUser("/queue/error")
    public Result CommonErrorHandler(OperationException e){
        return Result.fail(e.getCode(), e.getMsg());
    }

}
