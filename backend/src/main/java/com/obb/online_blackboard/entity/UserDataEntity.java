package com.obb.online_blackboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.obb.online_blackboard.entity.base.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author 陈桢梁
 * @desc UserDataEntity.java
 * @date 2022-10-27 09:11
 * @logs[0] 2022-10-27 09:11 陈桢梁 创建了UserDataEntity.java文件
 */
@Data
@RedisHash("user_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataEntity extends Date {

    private long id;

    @Id
    private long userId;

    private String nickname;

    private Integer avatar;

    private int isAnonymous;

    private String status;

    private Long nowRoom;

    private FileEntity entity;

}
