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


    @Select("select r.* from room r " +
            "left join room_setting rs " +
            "on rs.room_id = r.id " +
            "where rs.start_time < #{time} " +
            "and r.loaded = 0")
    @Results({
            @Result(column = "id", property = "setting",
                    many = @Many(select = "com.obb.online_blackboard.dao.mysql.RoomSettingDao.getByRoomId")),
            @Result(column = "id", property = "id")

    })
    List<RoomEntity> preloadRoom(Date time);


    @Update("<script>" +
                "update room_setting set " +
                    "<if test='startTime != null'>start_time = #{startTime},</if>" +
                    "<if test='endTime != null'>end_time = #{endTime},</if>" +
//                    "<if test='allowAnonymous != null'>allow_anonymous = #{allowAnonymous},</if>" +
                    "<if test='isShare != null'>is_share = #{isShare},</if>" +
                    "id = #{id}" +
                    "where id = #{id}" +
            "</script>")
    void update(RoomSettingEntity roomSettingEntity);
}
