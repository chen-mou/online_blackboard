package com.obb.online_blackboard.service;

import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.RoomModel;
import com.obb.online_blackboard.model.SheetModel;
import com.obb.online_blackboard.model.UserModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.result.Message;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc RoomService.java
 * @date 2022-10-27 18:29
 * @logs[0] 2022-10-27 18:29 陈桢梁 创建了RoomService.java文件
 */
@Service
public class RoomService {

    @Resource
    RoomModel roomModel;

    @Resource
    UserModel userModel;

    @Resource
    SheetService sheetService;

    @Resource
    UserDao userDao;


    @Resource
    SimpMessagingTemplate template;

    @Transactional
    public RoomEntity createRoom(RoomSettingEntity setting, long userId){
        RoomEntity room = new RoomEntity();
        UserDataEntity user = userModel.getDataById(userId);
        room.setCreatorId(userId);
        room.setCreatorName(user.getNickname());
        room.setName(setting.getName());
        //创建并获取房间ID
        String roomId = roomModel.createRoom(room);

        roomModel.createSetting(setting, userId, roomId);
        room.setSetting(setting);
        return room;
    }

    public RoomEntity joinRoom(String roomId, long userId, int isAnonymous){
        RoomEntity r = roomModel.getRoomById(roomId);
        if(r == null) {
            throw new OperationException(404, "目标房间不存在");
        }
        if(!r.getStatus().equals("meeting")){
            throw new OperationException(404, "会议未开始或已经结束了");
        }
        RoomSettingEntity setting = roomModel.getRoomSettingByRoomId(roomId);
        if(setting.getAllowAnonymous() == 0 && isAnonymous == 1){
            throw new OperationException(403, "目标房间不能匿名");
        }
        UserDataEntity user = userModel.getUserById(userId);
        user.setIsAnonymous(isAnonymous);
        user.setStatus("online");
        user.setNowRoom(roomId);
        userModel.saveData(user);
        if(isAnonymous == 1){
            user.setNickname("匿名用户");
        }
        if(!inRoom(r, userId)){
            template.convertAndSend("/exchange/room/" + roomId, Message.def("user_join", user));
        }
        return r;
    }

    public void over(String roomId, long userId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(!room.getStatus().equals("meeting")){
            throw new OperationException(404, "会议未开始或已经结束");
        }
        if(room.getCreatorId() != userId){
            throw new OperationException(403, "没有结束会议的权限");
        }
        roomModel.delRoom(roomId);
        template.convertAndSend("/exchange/room/" + roomId, Message.def("over", null));
    }

    public RoomEntity roomInfo(long userId, String roomId){
        RoomEntity r = roomModel.getRoomById(roomId);
        if(r == null) {
            throw new OperationException(404, "目标房间不存在");
        }
        if(!r.getStatus().equals("meeting")){
            throw new OperationException(404, "会议未开始或已经结束了");
        }
        if(!inRoom(r, userId)){
            throw new OperationException(403, "不在房间中不能获取房间信息");
        }
        template.convertAndSendToUser(String.valueOf(userId), "/queue/info", r);
        return r;
    }

    public List<RoomEntity> getUserRoom(long userId){
        return roomModel.getByCreator(userId);
    }

    public void updateSetting(RoomSettingEntity setting, long userId){
        RoomEntity r = roomModel.getRoomById(setting.getRoomId());
        if(r.getCreatorId() != userId){
            throw new OperationException(403, "只有创建者才能修改设置");
        }
        if(!r.getStatus().equals("meeting")){
            throw new OperationException(404, "会议未开始或已结束");
        }
        roomModel.updateRoomSetting(setting);
        if(setting.getIsShare() != null){
            switch(setting.getIsShare()){
                case 0:
                    SheetEntity sheet = sheetService.getSheetById(r.getNowSheet(), r.getId(), userId);
                    template.convertAndSend("/exchange/room/" + r.getId(), Message.def("change_sheet", sheet));
            }
        }
    }

    private boolean inRoom(RoomEntity room, long userId){
        UserDataEntity data = userModel.getDataById(userId);
        return data.getNowRoom().equals(room.getId());
    }

}
