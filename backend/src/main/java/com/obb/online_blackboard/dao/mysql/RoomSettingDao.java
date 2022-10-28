package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc RoomSettingDao.java
 * @date 2022-10-27 17:29
 * @logs[0] 2022-10-27 17:29 陈桢梁 创建了RoomSettingDao.java文件
 */
@Mapper
public interface RoomSettingDao {

    @Insert("insert into room_setting " +
            "value(#{id}, #{roomId}, #{creatorId}, #{isShare}, #{allowAnonymous}, #{startTime}, #{endTime})")
    int createSetting(RoomSettingEntity setting);

    @Select("select * from room_setting where room_id = #{roomId}")
    RoomSettingEntity getByRoomId(long roomId);


    @Select("select * from room r " +
            "left join room_setting rs " +
            "on rs.room_id = r.id " +
            "where rs.start_time > #{time} " +
            "and r.loaded = 0")
    List<RoomEntity> preloadRoom(Date time);
}
