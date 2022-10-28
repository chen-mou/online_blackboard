package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.RoomEntity;
import org.apache.ibatis.annotations.*;

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
    List<RoomEntity> getRoomByCreatorId(long creatorId);

    @Select("select * from room where id = #{id}")
    RoomEntity getRoomById(long room);

    @Update("<script>" +
                "update room set" +
                    "<if test = 'loaded != null'>loaded = #{loaded},</if>" +
                    "<if test = 'name != null'>name = #{name},</if>" +
                    "<if test = 'status != null'>status = #{status},</if>" +
                    "creator_name = creator_name" +
                    "where id in(" +
                        "<foreach collection='rooms' item='item' separator=','>" +
                            "#{item.id}" +
                        "</foreach>" +
                    ")" +
            "</script>")
    void update(@Param("rooms") List<RoomEntity> rooms);

}
