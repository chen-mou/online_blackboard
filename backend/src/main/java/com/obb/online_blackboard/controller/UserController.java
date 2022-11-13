package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.service.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xerial.snappy.Snappy;
import tool.annotation.JsonKey;
import tool.annotation.NotNeedLogin;
import tool.annotation.UserInfo;
import tool.result.Result;
import tool.util.JWT;
import tool.util.ParamError;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public Result login(@RequestBody @Validated UserEntity user, Errors errors) {
        ParamError.handlerError(errors);
        UserDataEntity data = userService.login(user.getUsername(), user.getPassword());
        String token = JWT.encode(new HashMap<>() {
            {
                put("userId", String.valueOf(data.getUserId()));
            }
        });
        return Result.success("登录成功", new HashMap<>() {
            {
                put("token", token);
                put("user_data", data);
            }
        });
    }

    @PostMapping("/register")
    @NotNeedLogin
    public Result register(@RequestBody @Validated UserEntity user, Errors errors) {
        ParamError.handlerError(errors);
        UserDataEntity data = userService.register(user.getUsername(), user.getPassword());
        String token = JWT.encode(new HashMap<>() {
            {
                put("userId", String.valueOf(data.getUserId()));
            }
        });
        return Result.success("注册成功", new HashMap<>() {
            {
                put("token", token);
                put("user_data", data);
            }
        });
    }

    @PostMapping("/logout")
    public Result logout(@UserInfo UserEntity user) {
        userService.logout(user.getId());
        return Result.success("登出成功", null);
    }

    @GetMapping("/info")
    public Result getUserInfo(@UserInfo UserEntity user) {
        return Result.success("获取成功", userService.getUserInfo(user.getId()));
    }

    @PostMapping("/nickname")
    public Result update(@RequestBody UserDataEntity userData, @UserInfo UserEntity user) {
        userService.updateNickname(userData.getNickname(), userData.getAvatar(), user.getId());
        return Result.success("修改成功", null);
    }

    @PostMapping("/getResult")
    public Result get(@JsonKey String value) throws IOException {
        byte[] val = Snappy.uncompress(value.getBytes(StandardCharsets.UTF_8));
        String msg = new String(val);
        System.out.println(msg);
        return Result.success("成功", msg);
    }

}
