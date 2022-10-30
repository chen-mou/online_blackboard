package com.obb.online_blackboard.service;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.RoomModel;
import com.obb.online_blackboard.model.SheetModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tool.util.MessageUtil;

import javax.annotation.Resource;

/**
 * @author 陈桢梁
 * @desc SheetService.java
 * @date 2022-10-29 10:35
 * @logs[0] 2022-10-29 10:35 陈桢梁 创建了SheetService.java文件
 */
@Service
public class SheetService {

    @Resource
    SheetModel sheetModel;

    @Resource
    RoomModel roomModel;

    @Resource
    SimpMessagingTemplate template;

    public SheetEntity createSheet(String roomId, String name, long userId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(room.getCreatorId() != userId){
            throw new OperationException(403, "非房间创建者不能创建画布");
        }
        if(!room.getStatus().equals("meeting")){
            throw new OperationException(403, "会议已经结束或者还未开始");
        }
        SheetEntity sheet = sheetModel.createSheet(name);
        room.getSheets().add(sheet.getId());
        template.convertAndSend("/" + room.getId() + "/create_sheet", sheet);
        roomModel.saveRoom(room);
        return sheet;
    }

    public void Draw(long userId, Shape shape, String roomId, long sheetId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(!room.getSheets().contains(sheetId)){
            throw new OperationException(404, "画布不存在");
        }
        if(!room.equals("meeting")){
            throw new OperationException(403, "会议已结束或者未没开始");
        }
        if(room.getSetting().getIsShare() == 0 && room.getCreatorId() != userId){
            throw new OperationException(403, "你没有编辑权限");
        }
        SheetEntity sheet = sheetModel.getSheetById(sheetId);
        sheet.addStack(userId, shape.getId());
    }

    public void set(){

    }

}