package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.FileDao;
import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.dao.redis.UserDataDao;
import com.obb.online_blackboard.entity.FileRoleEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.exception.OperationException;
import org.apache.catalina.User;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tool.encrypt.MD5;
import tool.util.id.Id;
import tool.util.lock.LockUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    UserDataDao userDataDao;

    @Resource
    FileDao fileDao;

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

    /**
     * 只能获取数据库的用户数据
     * @param userId 用户ID
     * @return
     */
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
    public void updateNickname(String newName, Integer avatar, long userId) {
        if(avatar != null){
            FileRoleEntity fileRole = fileDao.userRoleFile("avatar", userId, avatar);
            if(fileRole == null){
                throw new OperationException(403, "这不是你的头像");
            }
        }
        userDao.updateNickname(newName, avatar, userId);
    }

    public List<UserDataEntity> getUserDataByRoomId(long roomId){
        return userDataDao.getAllByNowRoom(roomId);
    }

    public UserDataEntity getUserById(long userId){
        Optional<UserDataEntity> op = userDataDao.findById(userId);
        if(op.isEmpty()){
            return userDao.getByUserId(userId);
        }
        return op.get();
    }

    public void saveData(UserDataEntity userData){
        userDataDao.save(userData);
    }

    public void saveAllData(List<UserDataEntity> userDataEntities){
        userDataEntities.forEach(item -> saveData(item));
    }
}
