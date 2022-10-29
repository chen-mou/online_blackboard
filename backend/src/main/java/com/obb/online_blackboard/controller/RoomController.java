package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.service.RoomService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.annotation.JsonKey;
import tool.annotation.UserInfo;
import tool.result.Result;
import tool.util.ParamError;

import javax.annotation.Resource;

/**
 * @author 陈桢梁
 * @desc RoomController.java
 * @date 2022-10-27 15:43
 * @logs[0] 2022-10-27 15:43 陈桢梁 创建了RoomController.java文件
 */

@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    RoomService roomService;
    @PostMapping("/create")
    public Result createRoom(@RequestBody @Validated RoomSettingEntity setting,
                             @UserInfo UserEntity user,
                             Errors errors){
        ParamError.handlerError(errors);
        RoomEntity room = roomService.createRoom(setting, user.getId());
        System.out.println("成功");
        return Result.success("创建成功", room);
    }

    @PostMapping("/join")
    public Result joinRoom(@UserInfo UserEntity user,
                           @JsonKey String roomId,
                           @JsonKey int isAnonymous){
        RoomEntity room = roomService.joinRoom(roomId, user.getId(), isAnonymous);
        return Result.success("加入成功", room);
    }

}
