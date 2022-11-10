package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.FileDao;
import com.obb.online_blackboard.entity.FileEntity;
import com.obb.online_blackboard.entity.FileRoleEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tool.util.id.Id;
import tool.util.lock.LockUtil;

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

    @Resource
    LockUtil<FileEntity> lock;

    @Resource
    RedisTemplate<String, Object> redis;

    @Value("${server.uri_pre}")
    String pre;

    private final String machine = System.getenv().get("COMPUTERNAME");

    private final String key = "FILE_GET:";

    @Transactional
    public FileEntity insert(long userId, String md5, String filename, String filePath, String type) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setMd5(md5);
        fileEntity.setId(id.getId("file"));
        fileEntity.setUri(pre + "/"+ machine + "/file/get/" + md5);
        fileEntity.setMachine(machine);
        fileEntity.setFilename(filename);
        fileEntity.setPath(filePath);
        fileEntity.setStatus("pending");
        fileDao.insert(fileEntity);
        redis.delete(key + md5);
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
        return lock.getLock(key + md5, "GET_FILE_LOCK:" + md5,
                () -> fileDao.getByMd5(md5), 5);
    }

    public FileEntity getById(long fileId){
        return fileDao.getByFileId(fileId);
    }

    public FileEntity updateStatus(long fileId, String status){
        fileDao.updateStatus(status, fileId);
        return fileDao.getByFileId(fileId);
    }

}
