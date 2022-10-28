package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.dao.mysql.RoomSettingDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;
import tool.util.lock.LockUtil;

import javax.annotation.Resource;

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
    Id id;

    @Resource
    LockUtil<RoomSettingEntity> lock;

    private final String SETTING_KEY = "setting_key:";

    private final String SETTING_LOCK = "setting_lock:";

    public String createRoom(RoomEntity room){
        room.setId(String.valueOf(id.getId("room")));
        roomDao.save(room);
        return room.getId();
    }

    public void saveRoom(RoomEntity room){
        roomDao.save(room);
    }

    public String createSetting(RoomSettingEntity setting, long userId, String roomId){
        setting.setId(String.valueOf(id.getId("setting")));
        setting.setCreatorId(userId);
        setting.setRoomId(roomId);
        roomSettingDao.createSetting(setting);
        return setting.getId();
    }

    public RoomEntity getRoomById(String roomId){
        return roomDao.getRoomEntityById(roomId);
    }

    public RoomSettingEntity getRoomSettingByRoomId(String roomId){
        return lock.getLock(SETTING_KEY + roomId,
                SETTING_LOCK + roomId,
                () -> roomSettingDao.getByRoomId(Long.parseLong(roomId)),3);
    }

}
