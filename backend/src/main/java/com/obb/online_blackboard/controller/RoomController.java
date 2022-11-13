package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tool.annotation.JsonKey;
import tool.annotation.UserInfo;
import tool.result.Message;
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
        Date end = setting.getEndTime();
        if(setting.isNow()){
            start = new Date();
            setting.setStartTime(start);
        }
        if(!setting.isNow() && start.getTime() < new Date().getTime() - 5 * 60 * 10000){
            throw new OperationException(500, "开始时间不能小于当前时间");
        }
        if(start.getTime() > end.getTime()){
            throw new OperationException(500, "开始时间不能大于结束时间");
        }
        if(end.getTime() - start.getTime() > 24 * 3600 * 1000){
            throw new OperationException(500, "会议时长不能大于1天");
        }

        RoomEntity room = roomService.createRoom(setting, user.getId());
//        System.out.println("成功");
        return Result.success("创建成功", room);
    }

    @PostMapping("/delete")
    public Result delete(@UserInfo UserEntity user, @JsonKey long roomId){
        roomService.delete(roomId, user.getId());
        return Result.success("删除成功", null);
    }

    @GetMapping("/inRoom")
    public Result inRoom(@RequestParam long userId, @RequestParam long roomId){
        roomService.verifyRoom(roomId, userId);
        return Result.success("成功", null);
    }

    @GetMapping("/my_room")
    public Result getMyRoom(@UserInfo UserEntity user){
        return Result.success("获取成功", roomService.getUserRoom(user.getId()));
    }

    @PostMapping("/update_setting")
    public Result UpdateSetting(@RequestBody RoomSettingEntity setting, @UserInfo UserEntity user){
        if(setting.getRoomId() == 0){
            throw new OperationException(500, "缺少房间ID");
        }
        roomService.updateSetting(setting, user.getId());
        return Result.success("修改成功", setting);
    }

    @Deprecated
//    @PostMapping("/join")
    public Result joinRoom(@UserInfo UserEntity user,
                           @JsonKey long roomId,
                           @JsonKey int isAnonymous){
        RoomEntity room = roomService.joinRoom(roomId, user.getId(), isAnonymous);
        return Result.success("加入成功", room);
    }

    //结束会议
    @MessageMapping("/over")
    public void over(Principal p, @JsonKey long roomId){
        roomService.over(roomId, Long.parseLong(p.getName()));
    }

    @MessageMapping("/room_info")
    public Result roomInfo(Principal p, @JsonKey long roomId){
        return Result.success("获取成功", roomService.roomInfo(Long.parseLong(p.getName()), roomId));
    }

    @MessageMapping("/quit")
    public void quit(Principal p, @JsonKey long roomId){
        long userId = Long.parseLong(p.getName());
        roomService.quit(userId, roomId);
    }

    @MessageMapping("/users")
    @SendToUser("/queue/info")
    public Message users(Principal p){
        long userId = Long.parseLong(p.getName());
        return Message.def("users", roomService.getRoomUser(userId));
    }

}
