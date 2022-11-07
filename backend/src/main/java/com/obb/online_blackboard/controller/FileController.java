package com.obb.online_blackboard.controller;

import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tool.annotation.NotNeedLogin;
import tool.annotation.UserInfo;
import tool.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 陈桢梁
 * @desc FileController.java
 * @date 2022-11-05 17:31
 * @logs[0] 2022-11-05 17:31 陈桢梁 创建了FileController.java文件
 */
@RestController
@RequestMapping("/file")
public class FileController {

    Set<String> allowSuffix = new HashSet<>(){
        {
            addAll(List.of(new String[]{"jpg", "png", "bmp", "jpeg"}));
        }
    };


    @Resource
    FileService fileService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file, @UserInfo UserEntity userData, String type){
        if(file.getSize() > 5 * 1024 * 1024){
            throw new OperationException(500, "文件过大");
        }
        String filename = file.getOriginalFilename();
        String typ = file.getContentType();
        if(!allowSuffix.contains(filename.split("\\.")[1]) || !typ.split("/").equals("image")){
            throw new OperationException(500, "文件类型有误");
        }
        fileService.upload(file, userData.getId(), type);
        return Result.success("上传成功", null);
    }

    @GetMapping("/get/{md5}")
    @NotNeedLogin
    public void get(HttpServletResponse res, @PathVariable String md5) throws IOException {
        fileService.get(res, md5);
    }

    @GetMapping("/getUserUpload")
    public Result getUserUpload(@UserInfo UserDataEntity userData, String type){
        return Result.success("获取成功", fileService.getUploadFile(userData.getUserId(), type));
    }

}
