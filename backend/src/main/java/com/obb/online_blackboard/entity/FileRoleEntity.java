package com.obb.online_blackboard.entity;

import lombok.Data;

/**
 * @author 陈桢梁
 * @desc FileRoleEntity.java
 * @date 2022-11-09 20:43
 * @logs[0] 2022-11-09 20:43 陈桢梁 创建了FileRoleEntity.java文件
 */
@Data
public class FileRoleEntity {

    private long id;

    private long userId;

    private long fileId;

    private String type;

    private FileEntity file;

}
