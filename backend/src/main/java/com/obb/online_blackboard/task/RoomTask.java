package com.obb.online_blackboard.task;

import com.obb.online_blackboard.dao.mysql.RoomDbDao;
import com.obb.online_blackboard.dao.mysql.RoomSettingDao;
import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.entity.*;
import com.obb.online_blackboard.model.RoomModel;
import com.obb.online_blackboard.model.SheetModel;
import com.obb.online_blackboard.model.UserModel;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    RoomModel roomModel;

    @Resource
    SheetModel sheetModel;

    @Resource
    RoomDbDao roomDbDao;

    @Resource
    UserModel userModel;

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
            item.setLoaded(1);
            try {
                File file = new File(sheetModel.path + "sheet-" + sheet.getId() + ".txt");
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
          roomDbDao.updateAll(rooms, 1);
        for(RoomEntity room : rooms){
            roomDao.save(room);
        }
    }

    @Scheduled(cron = "* * */20 * * *")
    public void cleanOverRoom(){
        Iterable<RoomEntity> iterable = roomDao.findAll();
        Date now = new Date();
        iterable.forEach(item -> {
            if(item == null){
                return;
            }
            List<UserDataEntity> users = userModel.getUserDataByRoomId(item.getId());
            if(users.size() == 0 && !item.getStatus().equals("no_start")){
                if(item.getSetting() == null){
                    item.setSetting(roomModel.getRoomSettingByRoomId(item.getId()));
                }
                if(item.getSetting().getEndTime().getTime() < now.getTime()) {
                    roomModel.delRoom(item.getId());
                }
            }
        });
    }

}
