package com.obb.online_blackboard.task;

import com.obb.online_blackboard.dao.mysql.RoomDbDao;
import com.obb.online_blackboard.dao.mysql.RoomSettingDao;
import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.RoomSettingEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.model.SheetModel;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 陈桢梁
 * @desc RoomTask.java
 * @date 2022-10-28 19:22
 * @logs[0] 2022-10-28 19:22 陈桢梁 创建了RoomTask.java文件
 */
@Component
public class RoomTask {

    @Resource
    RoomSettingDao settingDao;

    @Resource
    RoomDao roomDao;

    @Resource
    SheetModel sheetModel;

    @Resource
    RoomDbDao roomDbDao;

    /**
     * 提前20分钟加载房间
     */
    @Scheduled(cron = "* */1 * * * *")
    public void preloadRoom(){
        Date now = DateUtils.addMinutes(new Date(), 20);
        List<RoomEntity> rooms = settingDao.preloadRoom(now);
        if(rooms.size() == 0){
            return;
        }
        rooms.forEach((item) -> {
            SheetEntity sheet= sheetModel.createSheet("sheet-1", item.getId());
            RoomSettingEntity setting = item.getSetting();
            //会议结束后房间保留三个小时，前一个小时可以编辑，后一个小时会不能编辑
            item.setTimeout(setting.getEndTime().getTime() - setting.getStartTime().getTime() + 3 * 60 * 60 * 1000);
            item.getSheets().add(sheet.getId());
            item.setNowSheet(sheet.getId());
            item.setStatus("meeting");
            item.setLoaded(1);
        });
        roomDbDao.updateAll(rooms, 1, "meeting");
        for(RoomEntity room : rooms){
            roomDao.save(room);
        }
    }

    @Scheduled(cron = "* * */1 * * *")
    public void cleanOverRoom(){
        ArrayList<RoomEntity> rooms = new ArrayList<>((Collection) roomDao.findAll());
        roomDbDao.cleanOver(new Date(), rooms);
    }

}
