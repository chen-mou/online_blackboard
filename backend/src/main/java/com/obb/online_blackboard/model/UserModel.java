package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;
import tool.util.lock.LockUtil;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 陈桢梁
 * @desc UserModel.java
 * @date 2022-10-27 14:29
 * @logs[0] 2022-10-27 14:29 陈桢梁 创建了UserModel.java文件
 */

@Repository
public class UserModel {
    @Resource
    LockUtil<UserEntity> lock;

    @Resource
    UserDao userDao;

    @Resource
    RedisTemplate<String, Object> redis;

    public static final String USER_KEY = "USER:";

    public static final String LOCK_KEY = "USER_LOCK:";

    public UserEntity getUserByName(String username){
        UserEntity user = userDao.getByName(username);
        redis.opsForValue().set(USER_KEY + user.getId(), user);
        return user;
    }

    public UserDataEntity getDataById(long userId){
        return userDao.getByUserId(userId);
    }

    @Transient
    public void createUser(UserEntity user){
        long userId = Id.getId("User");
        user.setCtime(new Date());
        user.setId(userId);
        UserDataEntity data = new UserDataEntity();
        data.setUserId(userId);
        userDao.createData(data);
        userDao.create(user);
    }


}
