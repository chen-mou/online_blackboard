package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.service.SheetService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.annotation.JsonKey;
import tool.annotation.UserInfo;
import tool.result.Message;
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

    @MessageMapping("/modify_sheet")
    public void modify(Principal principal, @JsonKey String room, @JsonKey long sheetId){}

    @MessageMapping("/draw")
    public void draw(Principal p, @JsonKey Shape shape, @JsonKey String roomId, @JsonKey long sheetId){
        sheetService.draw(Long.parseLong(p.getName()), shape, roomId, sheetId);
    }

    @MessageMapping("/redo")
    public void redo(Principal p, @JsonKey String roomId, @JsonKey long sheetId){
        sheetService.redo(Long.parseLong(p.getName()), roomId, sheetId);
    }

    @MessageMapping("/rollback")
    public void rollback(Principal p, @JsonKey String roomId, @JsonKey long sheetId){
        sheetService.rollback(Long.parseLong(p.getName()), roomId, sheetId);
    }

    @MessageMapping("/modify")
    public void setShape(Principal p, @JsonKey Shape shape, @JsonKey String roomId, @JsonKey long sheetId) {
        sheetService.modify(Long.parseLong(p.getName()), shape, roomId, sheetId);
    }

    @MessageMapping("/delete")
    public void delete(Principal p, @JsonKey long shapeId, @JsonKey String roomId, @JsonKey long sheetId){
        sheetService.delete(Long.parseLong(p.getName()), roomId, sheetId, shapeId);
    }


}
