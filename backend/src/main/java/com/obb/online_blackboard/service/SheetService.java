package com.obb.online_blackboard.service;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.RoomModel;
import com.obb.online_blackboard.model.ShapeModel;
import com.obb.online_blackboard.model.SheetModel;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tool.annotation.Lock;
import tool.result.Message;
import tool.util.MessageUtil;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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

    @Resource
    ShapeModel shapeModel;

    @Resource
    RedissonClient redissonClient;

    private void verifyCreator(long userId, String roomId, long sheetId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(room == null){
            throw new OperationException(404, "房间不存在");
        }
        if(!room.getSheets().contains(sheetId)){
            throw new OperationException(404, "画布不存在");
        }
        if(room.getNowSheet() != sheetId){
            throw new OperationException(403, "无法编辑没选中的画布");
        }
        if(!room.getStatus().equals("meeting")){
            throw new OperationException(403, "会议已结束或者未没开始");
        }
        if(room.getSetting().getIsShare() == 0 && room.getCreatorId() != userId){
            throw new OperationException(403, "你没有编辑权限");
        }
        for(UserDataEntity ude : room.getParticipants()){
            if(ude.getUserId() == userId){
                if(ude.getIsAnonymous() == 1){
                    throw new OperationException(403, "匿名用户不能操作");
                }
                break;
            }
        }
    }

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
        template.convertAndSend("/exchange/room/" + room.getId() + "/create_sheet", sheet);
        roomModel.saveRoom(room);
        return sheet;
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void draw(long userId, Shape shape, String roomId, long sheetId){
        verifyCreator(userId, roomId, sheetId);
        SheetEntity sheet = sheetModel.getSheetById(sheetId);
        shapeModel.createShape(shape);
        sheet.addStack(userId, shape.getId());
        sheetModel.save(sheet);
        template.convertAndSend("/exchange/room/" + roomId, Message.add(shape));
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void rollback(long userId, String roomId, long sheetId){
        verifyCreator(userId, roomId, sheetId);
        SheetEntity sheet = sheetModel.getSheetById(sheetId);
        sheet.rollback(userId);
        sheetModel.save(sheet);
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void redo(long userId, String roomId, long sheetId) {
        verifyCreator(userId, roomId, sheetId);
        SheetEntity sheet = sheetModel.getSheetById(sheetId);
        sheet.redo(userId);
        sheetModel.save(sheet);

    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void modify(long userId, Shape shape, String roomId, long sheetId){
        verifyCreator(userId, roomId, sheetId);
        long id = shape.getId();
        Shape s = shapeModel.createShape(shape);
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void delete(long userId, long roomId, long sheetId, long shapeId){

    }

}
