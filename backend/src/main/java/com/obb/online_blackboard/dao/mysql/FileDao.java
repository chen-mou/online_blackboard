package com.obb.online_blackboard.dao.mysql;

import com.obb.online_blackboard.entity.FileEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Insert("insert into file value(#{id}, #{filename}, #{path}, #{uri}, #{uploader}, #{md5}, #{machine})")
    void insert(FileEntity file);

    @Select("select filename, path, uri, uploader where uploader = #{userId} and type = #{type}")
    List<FileEntity> getByUploader(long userId, String type);

}
