package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.FileDao;
import com.obb.online_blackboard.entity.FileEntity;
import com.obb.online_blackboard.entity.FileRoleEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tool.util.id.Id;

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

    @Resource
    Id id;

    @Value("${server.uri_pre}")
    String pre;

    private final String machine = System.getenv().get("COMPUTERNAME");

    @Transactional
    public FileEntity insert(long userId, String md5, String filename, String filePath, String type) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setMd5(md5);
        fileEntity.setId(id.getId("file"));
        fileEntity.setUri(pre + "/file/get/" + md5);
        fileEntity.setMachine(machine);
        fileEntity.setFilename(filename);
        fileEntity.setPath(filePath);
        fileDao.insert(fileEntity);
        insertRole(userId, fileEntity.getId(), type);
        return fileEntity;
    }

    public FileRoleEntity insertRole(long userId, long fileId, String type){
        FileRoleEntity fileRoleEntity = new FileRoleEntity();
        fileRoleEntity.setUserId(userId);
        fileRoleEntity.setType(type);
        fileRoleEntity.setFileId(fileId);
        fileDao.insertRole(fileRoleEntity);
        return fileRoleEntity;
    }

    public List<FileEntity> getByUserId(long userId, String type){
        return fileDao.getByUploader(userId, type);
    }

    public FileEntity getByMd5(String md5){
        return fileDao.getByMd5(md5);
    }

}
