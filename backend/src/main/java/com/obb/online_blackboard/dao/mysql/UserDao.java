package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import org.apache.ibatis.annotations.*;

/**
 * @author 陈桢梁
 * @desc UserDao.java
 * @date 2022-10-27 09:31
 * @logs[0] 2022-10-27 09:31 陈桢梁 创建了UserDao.java文件
 */
@Mapper
public interface UserDao {

    @Insert("insert into user(id, username, password, ctime) value(#{id}, #{username}, #{password}, #{ctime})")
    int create(UserEntity user);

    @Select("select name, password from user where username = #{name}")
    UserEntity getByName(String name);

    @Select("select nickname from user_data where user_id = #{userId}")
    UserDataEntity getById(long userId);

    @Insert("insert into user_data value(#{id}, #{userId}, #{nickname})")
    int createData(UserDataEntity userData);

}
