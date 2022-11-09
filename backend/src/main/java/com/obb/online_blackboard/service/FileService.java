package com.obb.online_blackboard.service;

import com.obb.online_blackboard.config.ThreadPool;
import com.obb.online_blackboard.entity.FileEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.FileModel;
import org.jboss.marshalling.ByteWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xerial.snappy.Snappy;
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
import java.util.concurrent.ThreadPoolExecutor;

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

    @Resource
    ThreadPoolExecutor pool;

    private final String path = "./file/";

    public FileEntity upload(MultipartFile file, long userId, String type) {
        String name = file.getOriginalFilename();
        byte[] b;
        byte[] zip;
        try{
            b = file.getBytes();
            zip = Snappy.compress(b);
        }catch (IOException e){
            throw new OperationException(500, "上传有误");
        }
        String md5 = MD5.salt(new String(zip));
        String filePath = path + md5;

        FileEntity fileEntity = fileModel.getByMd5(md5);
        if(fileEntity == null){
            fileModel.insert(userId, md5, name, filePath, type);
            pool.execute(() -> {
                try {
                    BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(filePath));
                    w.write(zip);
                    w.close();
                }catch (IOException e){

                }
            });
        }else{
            fileModel.insertRole(userId, fileEntity.getId(), type);
        }
        return fileEntity;
    }


    public void get(HttpServletResponse res, String md5) throws IOException {
        FileEntity file = fileModel.getByMd5(md5);
        String suffix = file.getFilename().split("\\.")[1];
        res.setContentType("image/" + suffix);
        InputStream is = new FileInputStream(file.getPath());
        OutputStream ops = res.getOutputStream();
        byte[] b = is.readAllBytes();
        byte[] unzip = Snappy.uncompress(b);
        ops.write(unzip);
    }

    public List<FileEntity> getUploadFile(long userId, String type){
        return fileModel.getByUserId(userId, type);
    }

}
