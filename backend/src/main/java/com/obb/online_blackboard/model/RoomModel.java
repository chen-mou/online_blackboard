package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.RoomDbDao;
import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.dao.mysql.RoomSettingDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;
import tool.util.lock.LockUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

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

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    UserModel userModel;

    @Resource
    ShapeModel shapeModel;

    @Resource
    ThreadPoolExecutor threadPoolExecutor;

    @Resource
    RedisKeyValueTemplate template;

    private final String SETTING_KEY = "setting_key:";

    private final String SETTING_LOCK = "setting_lock:";

    public String createRoom(RoomEntity room){
        room.setId(String.valueOf(id.getId("room")));
        room.setStatus("no_start");
        room.setLoaded(0);
        roomDbDao.insert(room);
        return room.getId();
    }

    public void saveRoom(RoomEntity room){
        roomDao.save(room);
    }

    public void updateRoom(String roomId, String key, Object value){
        PartialUpdate<RoomEntity> update = new PartialUpdate<>(roomId, RoomEntity.class).set(key, value);
        template.update(update);
    }

    public String createSetting(RoomSettingEntity setting, long userId, String roomId){
        setting.setId(String.valueOf(id.getId("room_setting")));
        setting.setCreatorId(userId);
        setting.setRoomId(roomId);
        String name = setting.getName();
        if(name == null || name.equals("")){
            UserDataEntity user = userModel.getDataById(userId);
            setting.setName(user.getNickname() + "的会议");
        }
        roomSettingDao.createSetting(setting);
        return setting.getId();
    }

    public RoomEntity getVerifyRoom(String roomId){
        Optional<RoomEntity> optional = roomDao.findById(roomId);
        RoomEntity r;
        if(optional.isEmpty()){
            r = roomDbDao.getRoomById(Long.parseLong(roomId));
        }else{
            r = optional.get();
            r.setSetting(roomSettingDao.getByRoomId(Long.parseLong(roomId)));
        }
        return r;
    }

    public RoomEntity getRoomById(String roomId){
        Optional<RoomEntity> optional = roomDao.findById(roomId);
        RoomEntity r;
        if(optional.isEmpty()){
            r = roomDbDao.getRoomById(Long.parseLong(roomId));
        }else{
            r = optional.get();
            SheetEntity nowSheet = sheetModel.getSheetById(r.getNowSheet());
            List<SheetEntity> sheetEntities = sheetModel.getAllByRoomId(roomId);
            r.setNowSheetEntity(nowSheet);
            r.setSheetEntities(sheetEntities);
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
        List<UserDataEntity> users = userModel.getUserDataByRoomId(roomId);
        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setStatus("over");
        roomDao.deleteById(roomId);
        roomDbDao.update(room);
        threadPoolExecutor.execute(() -> {
            users.forEach(item -> item.setNowRoom(null));
            userModel.saveAllData(users);
            List<SheetEntity> s = sheetModel.getAllByRoomId(roomId);
            s.forEach(item -> {
                List<Shape> shapes = shapeModel.getBySheetId(item.getId());
                shapes.forEach(item1 -> shapeModel.delete(item1.getId()));
            });
        });
    }

    public void updateRoomSetting(RoomSettingEntity setting){
        String key = SETTING_KEY + setting.getRoomId();
        redisTemplate.delete(key);
        roomSettingDao.update(setting);
        redisTemplate.delete(key);
    }

    public List<RoomEntity> getByCreator(long userId){
        return roomDbDao.getRoomByCreatorId(userId);
    }

}
