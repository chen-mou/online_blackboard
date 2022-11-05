package com.obb.online_blackboard.service;

import com.obb.online_blackboard.entity.FileEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.FileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tool.encrypt.MD5;
import tool.util.id.Id;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc FileService.java
 * @date 2022-11-05 18:52
 * @logs[0] 2022-11-05 18:52 陈桢梁 创建了FileService.java文件
 */
@Service
public class FileService {


    @Resource
    Id id;

    @Resource
    FileModel fileModel;

    @Value("server.uri_pre")
    String pre;

    private final String path = "./file/";

    private final String machine = System.getenv().get("COMPUTERNAME");

    public FileEntity upload(MultipartFile file, long userId, String type) {
        String name = file.getName();
        String md5 = MD5.salt(userId + name + new Date().getTime() + machine);
        String filePath = path + md5;
        try {
            file.transferTo(new File(filePath));
        }catch (IOException e){
            throw new OperationException(500, "上传有误");
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUploader(userId);
        fileEntity.setMd5(md5);
        fileEntity.setId(id.getId("file"));
        fileEntity.setUri(pre + "/file/get/" + md5);
        fileEntity.setMachine(machine);
        fileEntity.setFilename(name);
        fileEntity.setPath(filePath);
        fileEntity.setType(type);
        fileModel.insert(fileEntity);
        return fileEntity;
    }


    public void get(HttpServletResponse res, String md5) throws IOException {
        FileEntity file = fileModel.getByMd5(md5);
        String suffix = file.getFilename().split("\\.")[1];
        res.setContentType("image/" + suffix);
        InputStream is = new FileInputStream(file.getPath());
        OutputStream ops = res.getOutputStream();
        ops.write(is.readAllBytes());
    }

    public List<FileEntity> getUploadFile(long userId, String type){
        return fileModel.getByUserId(userId, type);
    }

}
