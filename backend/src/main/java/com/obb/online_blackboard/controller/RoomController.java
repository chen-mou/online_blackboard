package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tool.annotation.JsonKey;
import tool.annotation.UserInfo;
import tool.result.Result;
import tool.util.ParamError;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;

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
        Date start = setting.getStartTime();
        if(start.getTime() < new Date().getTime() - 5 * 60 * 10000){
            throw new OperationException(500, "开始时间不能小于当前时间");
        }
        RoomEntity room = roomService.createRoom(setting, user.getId());
//        System.out.println("成功");
        return Result.success("创建成功", room);
    }

    @GetMapping("/my_room")
    public Result getMyRoom(@UserInfo UserEntity user){
        return Result.success("获取成功", roomService.getUserRoom(user.getId()));
    }

    @PostMapping("/update_setting")
    public Result UpdateSetting(@RequestBody RoomSettingEntity setting){
        roomService.updateSetting(setting);
        return Result.success("修改成功", setting);
    }

    @Deprecated
    @PostMapping("/join")
    public Result joinRoom(@UserInfo UserEntity user,
                           @JsonKey String roomId,
                           @JsonKey int isAnonymous){
        RoomEntity room = roomService.joinRoom(roomId, user.getId(), isAnonymous);
        return Result.success("加入成功", room);
    }

    //结束会议
    @MessageMapping("/over")
    public Result over(Principal p, @JsonKey String roomId){
        roomService.over(roomId, Long.parseLong(p.getName()));
        return Result.success("会议已结束", null);
    }

    @MessageMapping("/room_info")
    public Result roomInfo(Principal p, @JsonKey String roomId){
        return Result.success("获取成功", roomService.roomInfo(Long.parseLong(p.getName()), roomId));
    }

}
