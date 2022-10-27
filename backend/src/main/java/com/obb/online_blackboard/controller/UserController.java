package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.result.Result;
import tool.util.JWT;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 陈桢梁
 * @desc UserController.java
 * @date 2022-10-27 14:48
 * @logs[0] 2022-10-27 14:48 陈桢梁 创建了UserController.java文件
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result login(String username, String password){
        UserDataEntity data = userService.login(username, password);
        String token = JWT.encode(null);
        return Result.success("登录成功", new HashMap<>(){
            {
                put("token", token)
                put("user_data", data);
            }
        });
    }

    @PostMapping("/register")
    public Result register(String username, String password){

    }

}
