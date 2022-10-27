package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.RoomEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author 陈桢梁
 * @desc RoomDao.java
 * @date 2022-10-27 17:28
 * @logs[0] 2022-10-27 17:28 陈桢梁 创建了RoomDao.java文件
 */
public interface RoomDao extends CrudRepository<RoomEntity, String> {

    RoomEntity getRoomEntityByRoomId(String roomId);
}
