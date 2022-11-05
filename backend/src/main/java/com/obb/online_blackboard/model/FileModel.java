package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.FileDao;
import com.obb.online_blackboard.entity.FileEntity;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc FileModel.java
 * @date 2022-11-05 19:31
 * @logs[0] 2022-11-05 19:31 陈桢梁 创建了FileModel.java文件
 */
@Repository
public class FileModel {

    @Resource
    FileDao fileDao;


    public void insert(FileEntity file){
        fileDao.insert(file);
    }

    public List<FileEntity> getByUserId(long userId, String type){
        return fileDao.getByUploader(userId, type);
    }

    public FileEntity getByMd5(String md5){
        return fileDao.getByMd5(md5);
    }

}
