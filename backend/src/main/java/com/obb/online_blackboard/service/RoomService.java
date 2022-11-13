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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.WebSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import tool.result.Message;
import tool.util.JWT;
import tool.util.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

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
    RedisTemplate<String, Object> redis;

    @Resource
    ThreadPoolExecutor pool;

    @Resource
    SimpMessagingTemplate template;

    @Value("${server.voice.key}")
    String VOICE_KEY;

    @Value("${server.voice.url}")
    String baseURL;

    public void verifyRoom(long roomId, long userId){
        RoomEntity r = roomModel.getRoomById(roomId);
        if(!r.getStatus().equals("meeting")){
            throw new OperationException(500, "会议未开始或已结束");
        }
        UserDataEntity user = userModel.getUserById(userId);
        if(user.getNowRoom() != roomId){
            throw new OperationException(500, "你不在当前房间内");
        }
    }

    @Transactional
    public RoomEntity createRoom(RoomSettingEntity setting, long userId){
        RoomEntity room = new RoomEntity();
        UserDataEntity user = userModel.getDataById(userId);
        room.setCreatorId(userId);
        room.setCreatorName(user.getNickname());
        //创建并获取房间ID
        long roomId = roomModel.createRoom(room);

        roomModel.createSetting(setting, userId, roomId);
        room.setSetting(setting);
        return room;
    }

    public RoomEntity joinRoom(long roomId, long userId, int isAnonymous){
        RoomEntity r = roomModel.getRoomById(roomId);
        if(r == null) {
            throw new OperationException(404, "目标房间不存在");
        }
        if(r.getLoaded() == 0 || r.getStatus().equals("over")){
            throw new OperationException(404, "会议未开始或已经结束了");
        }
        RoomSettingEntity setting = roomModel.getRoomSettingByRoomId(roomId);
        if(setting.getAllowAnonymous() == 0 && isAnonymous == 1){
            throw new OperationException(403, "目标房间不能匿名");
        }
        if(isAnonymous == 1 && r.getCreatorId() == userId){
            throw new OperationException(500, "创建者不能匿名");
        }
        UserDataEntity user = userModel.getUserById(userId);
        user.setIsAnonymous(isAnonymous);
        user.setStatus("online");
        user.setNowRoom(roomId);
        if(isAnonymous == 1){
            user.setNickname("匿名用户");
        }
        if(r.getStatus().equals("no_start")){
            roomModel.updateRoom(roomId, "status", "meeting");
            roomModel.updateStatus("meeting", roomId);
        }
        if(!inRoom(r.getId(), userId)){
            template.convertAndSend("/exchange/room/" + roomId, Message.def("user_join", user));
        }
        userModel.saveData(user);
        return r;
    }

    public void over(long roomId, long userId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(!room.getStatus().equals("meeting")){
            throw new OperationException(404, "会议未开始或已经结束");
        }
        if(room.getCreatorId() != userId){
            throw new OperationException(403, "没有结束会议的权限");
        }
        List<UserDataEntity> users = userModel.getUserDataByRoomId(roomId);
        roomModel.delRoom(roomId);
        template.convertAndSend("/exchange/room/" + roomId, Message.def("over", null));
        pool.execute(() -> {
            users.forEach((item) -> {
                closeSession(item.getUserId());
//                session.
            });
        });

    }

    public RoomEntity roomInfo(long userId, long roomId){
        RoomEntity r = roomModel.getRoomById(roomId);
        if(r == null) {
            throw new OperationException(404, "目标房间不存在");
        }
        if(!r.getStatus().equals("meeting")){
            throw new OperationException(404, "会议未开始或已经结束了");
        }
        if(!inRoom(r.getId(), userId)){
            throw new OperationException(403, "不在房间中不能获取房间信息");
        }
//        String url = getVoiceUrl(roomId, userId);
        template.convertAndSendToUser(String.valueOf(userId), "/queue/info", Message.def("room_info", r));
//        template.convertAndSendToUser(String.valueOf(userId), "/queue/info", Message.def("voice_url", url));
        return r;
    }

    public List<UserDataEntity> getRoomUser(long userId){
        UserDataEntity userData = userModel.getUserById(userId);
        List<UserDataEntity> roomUser = userModel.getUserDataByRoomId(userData.getNowRoom());
        return roomUser;
    }

    public List<RoomEntity> getUserRoom(long userId){
        return roomModel.getByCreator(userId);
    }

    public void updateSetting(RoomSettingEntity setting, long userId){
        RoomEntity r = roomModel.getRoomById(setting.getRoomId());
        if(r == null){
            throw new OperationException(404, "房间不存在");
        }
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

    public void delete(long roomId, long userId){
        RoomEntity r = roomModel.getVerifyRoom(roomId);
        if(r == null){
            throw new OperationException(500, "房间不存在");
        }
        if(r.getCreatorId() != userId){
            throw new OperationException(403, "你没有权限删除这个房间");
        }
        if(r.getStatus().equals("meeting")){
            throw new OperationException(500, "会议中,请结束会议后再删除房间");
        }
        roomModel.deleteDb(roomId);
        if(r.getLoaded() == 1){}
    }

    public void quit(long userId, long roomId){
        if(!inRoom(roomId, userId)){
            throw new OperationException(403, "你不在这个房间中");
        }
        RoomEntity r = roomModel.getVerifyRoom(roomId);
        if(r.getCreatorId() == userId){
            over(roomId, userId);
        }else{
            closeSession(userId);
            template.convertAndSend("/exchange/room/" + roomId, Message.def("user_quit", userId));
        }
    }

    public RoomSettingEntity getSettingByRoomId(long roomId){
        return roomModel.getRoomSettingByRoomId(roomId);
    }

    public void closeSession(long userId){
        String sessionID = (String)redis.opsForValue().get("user_session:" + userId);
        ConcurrentWebSocketSessionDecorator session = WebSocketSession.getSession(sessionID);
        if(session == null){
            return;
        }
        if(session.isOpen()){
            try {
                session.close(CloseStatus.NORMAL);
            } catch (IOException e) {
                throw new OperationException(500, e.getMessage());
            }
        }
        redis.delete("user_session:" + userId);
        redis.delete("session:" + sessionID);
    }

    private boolean inRoom(long roomId, long userId){
        UserDataEntity data = userModel.getUserById(userId);
        Long nowRoom = data.getNowRoom();
        return nowRoom != null && nowRoom == roomId;
    }

    private String getVoiceUrl(long roomId, long userId){
        String token = JWT.encode(new HashMap<>(){
            {
                put("roomId", String.valueOf(roomId));
                put("userId", String.valueOf(userId));
            }
        }, "", VOICE_KEY);
        return baseURL + "/live/" + roomId+"?token=" + token;
    }

}
