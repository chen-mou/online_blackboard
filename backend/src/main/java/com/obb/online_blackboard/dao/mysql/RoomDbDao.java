package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import org.apache.ibatis.annotations.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc RoomDao.java
 * @date 2022-10-28 18:19
 * @logs[0] 2022-10-28 18:19 陈桢梁 创建了RoomDao.java文件
 */
@Mapper
public interface RoomDbDao {

    @Insert("insert into room value(#{id}, #{creatorId}, #{creatorName}, #{name}, #{status}, #{loaded})")
    int insert(RoomEntity room);

    @Select("select * from room where creator_id = #{creatorId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "setting",
                    one = @One(select = "com.obb.online_blackboard.dao.mysql.RoomDbDao.getStartEndTimeById"))
    })
    List<RoomEntity> getRoomByCreatorId(long creatorId);

    @Select("select * from room where id = #{id}")
    RoomEntity getRoomById(long room);

    @Update("<script>" +
                "update room set " +
                    "loaded = #{loaded}, " +
                    "status = #{status} " +
                    "where id in(" +
                        "<foreach collection='rooms' item='item' separator=','>" +
                            "#{item.id}" +
                        "</foreach>" +
                    ")" +
            "</script>")
    void updateAll(@Param("rooms") List<RoomEntity> rooms, int loaded, String status);

    @Update("<script>" +
            "update room set " +
            "<if test = 'loaded != null'>loaded = #{loaded},</if> " +
            "<if test = 'name != null'>name = #{name},</if> " +
            "<if test = 'status != null'>status = #{status},</if> " +
            "creator_name = creator_name " +
            "where id = #{id}" +
            "</script>")
    void update(RoomEntity room);


    @Update("<script>" +
                "update room set " +
                    "status = 'over' " +
                "where " +
                " id in(select id from room_setting where end_time &lt; #{time})" +
                " <if test='rooms.size != 0'>" +
                    "and id not in(" +
                        "<foreach collection='rooms' item='item' separator=','>" +
                            "#{item.id}" +
                        "</foreach>" +
                        ")" +
                "</if>" +
            "</script>")
    void cleanOver(@Param("time") Date time, @Param("rooms") ArrayList<RoomEntity> rooms);

    @Select("select start_time, end_time from room_setting where room_id = #{roomId}")
    RoomSettingEntity getStartEndTimeById(long roomId);

}
