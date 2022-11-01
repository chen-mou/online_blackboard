package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.RoomDbDao;
import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.dao.mysql.RoomSettingDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;
import tool.util.lock.LockUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author 陈桢梁
 * @desc RoomModel.java
 * @date 2022-10-27 18:29
 * @logs[0] 2022-10-27 18:29 陈桢梁 创建了RoomModel.java文件
 */
@Repository
public class RoomModel {

    @Resource
    RoomDao roomDao;

    @Resource
    RoomSettingDao roomSettingDao;

    @Resource
    RoomDbDao roomDbDao;

    @Resource
    Id id;

    @Resource
    LockUtil<RoomSettingEntity> lock;

    @Resource
    SheetModel sheetModel;

    private final String SETTING_KEY = "setting_key:";

    private final String SETTING_LOCK = "setting_lock:";

    public String createRoom(RoomEntity room){
        room.setId(String.valueOf(id.getId("room")));
        room.setStatus("no_start");
        String name = room.getName();
        if(name == null || name.equals("")){
            room.setName(room.getCreatorName() + "的会议");
        }
        roomDbDao.insert(room);
        return room.getId();
    }

    public void saveRoom(RoomEntity room){
        roomDao.save(room);
    }

    public String createSetting(RoomSettingEntity setting, long userId, String roomId){
        setting.setId(String.valueOf(id.getId("room_setting")));
        setting.setCreatorId(userId);
        setting.setRoomId(roomId);
        roomSettingDao.createSetting(setting);
        return setting.getId();
    }

    public RoomEntity getRoomById(String roomId){
        Optional<RoomEntity> optional = roomDao.findById(roomId);
        RoomEntity r;
        if(optional.isEmpty()){
            r = roomDbDao.getRoomById(Long.parseLong(roomId));
        }else{
            r = optional.get();
            SheetEntity nowSheet = sheetModel.getSheetById(r.getNowSheet());
            r.setNowSheetEntity(nowSheet);
            r.setSetting(roomSettingDao.getByRoomId(Long.parseLong(roomId)));
        }
        return r;
    }

    public RoomSettingEntity getRoomSettingByRoomId(String roomId){
        return lock.getLock(SETTING_KEY + roomId,
                SETTING_LOCK + roomId,
                () -> roomSettingDao.getByRoomId(Long.parseLong(roomId)),3);
    }

    public void delRoom(String roomId){
        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setStatus("over");
        roomDao.deleteById(roomId);
        roomDbDao.update(room);
    }

    public void updateRoomSetting(RoomSettingEntity setting, long roomId){

    }

}
