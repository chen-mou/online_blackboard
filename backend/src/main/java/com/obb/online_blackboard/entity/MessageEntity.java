package com.obb.online_blackboard.entity;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @desc     MessageEntity.java
 * @author  陈桢梁
 * @date    2022-11-10 13:06
 * @logs[0] 2022-11-10 13:06 陈桢梁 创建了MessageEntity.java文件
 */
@Data
public class MessageEntity {

    private long sender;

    private long getter;

    private String type;

    private String msg;

    private String senderName;

    private String getterName;

    private boolean broadcast;

    private Date time;

    private FileEntity file;
}
