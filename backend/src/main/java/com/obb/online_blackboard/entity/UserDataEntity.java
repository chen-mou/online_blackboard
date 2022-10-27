package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Date;
import lombok.Data;

/**
 * @author 陈桢梁
 * @desc UserDataEntity.java
 * @date 2022-10-27 09:11
 * @logs[0] 2022-10-27 09:11 陈桢梁 创建了UserDataEntity.java文件
 */
@Data
public class UserDataEntity extends Date {

    private long id;

    private long userId;

    private String nickname;

    private int isAnonymous;

}
