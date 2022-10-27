package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.RoomSettingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc RoomSettingDao.java
 * @date 2022-10-27 17:29
 * @logs[0] 2022-10-27 17:29 陈桢梁 创建了RoomSettingDao.java文件
 */
@Repository
public interface RoomSettingDao extends CrudRepository<RoomSettingEntity, String> {

    RoomSettingEntity getRoomSettingEntityByRoomId(String roomId);
}
