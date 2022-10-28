package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.RoomSettingEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 陈桢梁
 * @desc RoomSettingDao.java
 * @date 2022-10-27 17:29
 * @logs[0] 2022-10-27 17:29 陈桢梁 创建了RoomSettingDao.java文件
 */
@Mapper
public interface RoomSettingDao{

    @Insert("insert into room_setting " +
            "value(#{id}, #{roomId}, #{creatorId}, #{isShare}, #{allowAnonymous}, #{startTime}, #{endTime})")
    int createSetting(RoomSettingEntity setting);

    @Select("select * from room_setting where room_id = #{roomId}")
    RoomSettingEntity getByRoomId(long roomId);
}
