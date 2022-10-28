package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.service.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.annotation.NotNeedLogin;
import tool.result.Result;
import tool.util.JWT;
import tool.util.ParamError;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    @NotNeedLogin
    public Result login(@RequestBody @Validated UserEntity user, Errors errors){
        ParamError.handlerError(errors);
        UserDataEntity data = userService.login(user.getUsername(), user.getPassword());
        String token = JWT.encode(new HashMap<>(){
            {
                put("userId", String.valueOf(data.getUserId()));
            }
        });
        return Result.success("登录成功", new HashMap<>(){
            {
                put("token", token);
                put("user_data", data);
            }
        });
    }

    @PostMapping("/register")
    @NotNeedLogin
    public Result register(@RequestBody @Validated UserEntity user, Errors errors){
        ParamError.handlerError(errors);
        UserDataEntity data = userService.register(user.getUsername(), user.getPassword());
        String token = JWT.encode(new HashMap<>(){
            {
                put("userId", String.valueOf(data.getUserId()));
            }
        });
        return Result.success("注册成功", new HashMap<>(){
            {
                put("token", token);
                put("user_data", data);
            }
        });
    }

}
