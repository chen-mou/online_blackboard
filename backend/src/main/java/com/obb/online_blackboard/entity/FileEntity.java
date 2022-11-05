package com.obb.online_blackboard.entity;

import lombok.Data;

/**
 * @author 陈桢梁
 * @desc FileEntity.java
 * @date 2022-11-05 18:50
 * @logs[0] 2022-11-05 18:50 陈桢梁 创建了FileEntity.java文件
 */
@Data
public class FileEntity {

    private long id;

    private long uploader;

    private String filename;

    private String machine;

    private String path;

    private String uri;

    private String md5;

    private String type;

}
