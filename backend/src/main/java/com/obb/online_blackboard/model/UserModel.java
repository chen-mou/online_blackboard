package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tool.encrypt.MD5;
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

    @Resource
    Id id;

    public static final String USER_KEY = "USER:";

    public static final String LOCK_KEY = "USER_LOCK:";

    public UserEntity getUserByName(String username) {
        UserEntity user = userDao.getByName(username);
        if (user == null) {
            return null;
        }
        redis.opsForValue().set(USER_KEY + user.getId(), user);
        return user;
    }

    public UserDataEntity getDataById(long userId) {
        return userDao.getByUserId(userId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void createUser(UserEntity user) {
        long userId = id.getId("User");
        user.setCtime(new Date());
        user.setId(userId);
        UserDataEntity data = new UserDataEntity();
        data.setUserId(userId);
        data.setAvatar(1);
        data.setNickname("初始名字" + MD5.salt(String.valueOf(userId)).substring(24));
        userDao.createData(data);
        userDao.create(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void updateNickname(String newName) {
        userDao.updateNickname(newName);
    }

}
