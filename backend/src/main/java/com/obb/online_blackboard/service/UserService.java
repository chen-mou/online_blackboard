package com.obb.online_blackboard.service;

import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.UserModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tool.encrypt.MD5;

import javax.annotation.Resource;

/**
 * @author 陈桢梁
 * @desc UserService.java
 * @date 2022-10-27 14:07
 * @logs[0] 2022-10-27 14:07 陈桢梁 创建了UserService.java文件
 */
@Service
public class UserService {

    @Resource
    UserModel userModel;

    @Resource
    RedisTemplate<String, Object> redis;

    private final int SALT_COUNT = 5;

    //登录并防止缓存穿透和缓存击穿
    public UserDataEntity login(String username, String password){
        UserEntity user = userModel.getUserByName(username);
        if(user == null){
           throw new OperationException(500, "用户名不存在");
        }
        if(MD5.salt(password, SALT_COUNT).equals(user.getPassword())){
            return userModel.getDataById(user.getId());
        }
        throw new OperationException(500, "密码有误");
    }

    public UserDataEntity register(String username, String password){
        UserEntity user = userModel.getUserByName(username);
        if(user != null){
            throw new OperationException(500, "用户名已经被人用了");
        }
        user = new UserEntity();
        password = MD5.salt(password, 5);
        user.setUsername(username);
        user.setPassword(password);
        userModel.createUser(user);
        return userModel.getDataById(user.getId());
    }

    public void logout(long userId){
        String token = (String)redis.opsForValue().get("token:" + userId);
        if(token == null){
            throw new OperationException(404, "你已经登出了");
        }
        redis.delete("token:" + userId);
    }

    public UserDataEntity getUserInfo(long userId){
        return userModel.getDataById(userId);
    }

}
