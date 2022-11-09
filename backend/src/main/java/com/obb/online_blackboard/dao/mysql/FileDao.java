package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.FileEntity;
import com.obb.online_blackboard.entity.FileRoleEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 陈桢梁
 * @desc FileDao.java
 * @date 2022-11-05 19:21
 * @logs[0] 2022-11-05 19:21 陈桢梁 创建了FileDao.java文件
 */
@Mapper
public interface FileDao {

    @Select("select * from file where md5 = #{md5}")
    FileEntity getByMd5(String md5);

    @Insert("insert into file value(#{id}, #{filename}, #{path}, #{uri}, #{md5}, #{machine})")
    void insert(FileEntity file);

    @Insert("insert into file_role(user_id, file_id, type) value(#{userId}, #{fileId}, #{type})")
    void insertRole(FileRoleEntity fileRoleEntity);

    @Select("select file_id where uploader = #{userId} and type = #{type}")
    @Results({
            @Result(column = "file_id", property = "fileId"),
            @Result(column = "file_id", property = "file",
                    one = @One(select = "com.obb.online_blackboard.dao.mysql.FileDao.getByFileId"))
    })
    List<FileEntity> getByUploader(long userId, String type);

    @Select("select * from file where id = #{fileId}")
    FileEntity getByFileId(long fileId);

}
