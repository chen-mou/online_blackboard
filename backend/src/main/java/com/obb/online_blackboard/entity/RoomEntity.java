package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Shape;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈桢梁
 * @desc RoomEntity.java
 * @date 2022-10-27 15:36
 * @logs[0] 2022-10-27 15:36 陈桢梁 创建了RoomEntity.java文件
 */
@Data
@RedisHash("room")
public class RoomEntity {

    @Id
    private String id;

    private long creatorId;

    private String creatorName;

    private String name;

    private Integer loaded;

//    private List<UserDataEntity> participants;

    private String status;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long timeout;

    private long nowSheet;

    @Transient
    private SheetEntity nowSheetEntity;

    Set<Long> sheets;

    List<SheetEntity> sheetEntities;

    @Transient
    RoomSettingEntity setting;


    public RoomEntity(){
        sheets = new HashSet<>();
    }

}
