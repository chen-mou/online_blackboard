package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.service.SheetService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.annotation.JsonKey;
import tool.annotation.UserInfo;
import tool.result.Result;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @author 陈桢梁
 * @desc SheetController.java
 * @date 2022-10-29 10:29
 * @logs[0] 2022-10-29 10:29 陈桢梁 创建了SheetController.java文件
 */
@RestController
public class SheetController {

    @Resource
    SheetService sheetService;

    @MessageMapping("/create")
    public void create(Principal principal, @JsonKey String name, @JsonKey String roomId){
        sheetService.createSheet(roomId, name, Long.parseLong(principal.getName()));
    }

    @MessageMapping("/draw")
    public void draw(Principal p, Shape s){

    }

    @MessageMapping("/set")
    public void setShape(Principal p, Shape s) {

    }

}
