package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author 陈桢梁
 * @desc RoomEntity.java
 * @date 2022-10-27 15:27
 * @logs[0] 2022-10-27 15:27 陈桢梁 创建了RoomEntity.java文件
 */
@Data
@RedisHash("room_setting")
public class RoomSettingEntity extends Date {

    @Id
    private String id;

    private String roomId;

    private String creatorName;

    private int isShare;

    private int allowAnonymous;

    private Date endTime;

}
