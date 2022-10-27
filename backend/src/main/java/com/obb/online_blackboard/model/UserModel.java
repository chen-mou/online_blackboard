package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import org.springframework.data.annotation.Transient;
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

    private final String USER_KEY = "USER:";

    private final String LOCK_KEY = "USER_LOCK:";

    public UserEntity getUserByName(String username){
        return lock.getLock(USER_KEY + ":" + username,
                LOCK_KEY + ":" + username,
                () -> userDao.getByName(username), 3);
    }

    public UserDataEntity getDataById(long userId){
        return userDao.getById(userId);
    }

    @Transient
    public void createUser(UserEntity user){
        user.setCtime(new Date());
        user.setId(Id.getId("User"));
        userDao.createData(new UserDataEntity());
        userDao.create(user);
    }


}
