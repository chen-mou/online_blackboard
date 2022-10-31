package com.obb.online_blackboard.service;

import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.RoomModel;
import com.obb.online_blackboard.model.SheetModel;
import com.obb.online_blackboard.model.UserModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.result.Message;
import tool.util.MessageUtil;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.Date;

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
    SheetModel sheetModel;

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
        for(UserDataEntity user : r.getParticipants()){
            if(user.getId() == userId){
                return r;
            }
        }
        UserDataEntity user = userDao.getByUserId(userId);
        user.setIsAnonymous(isAnonymous);
        if(isAnonymous == 1){
            user.setNickname("匿名用户");
        }
        r.getParticipants().add(user);
        roomModel.saveRoom(r);
        template.convertAndSend("/" + roomId, Message.def("user_join", user));
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
        template.convertAndSend("/" + roomId, Message.def("over", null));
    }


}
