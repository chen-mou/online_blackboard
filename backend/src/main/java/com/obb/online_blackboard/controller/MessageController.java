package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.MessageEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @desc     MessageController.java
 * @author  陈桢梁
 * @date    2022-11-10 13:04
 * @logs[0] 2022-11-10 13:04 陈桢梁 创建了MessageController.java文件
 */
@RestController
public class MessageController {

    @Resource
    MessageService messageService;

    private final Set<String> types = new HashSet<>(List.of(new String[]{"message", "url", "emoji"}));

    @MessageMapping("/send")
    public void send(Principal p, @Payload MessageEntity msg){
        if(!types.contains(msg.getType())){
            throw new OperationException(500, "参数有误");
        }
        messageService.send(Long.parseLong(p.getName()), msg);
    }

}
