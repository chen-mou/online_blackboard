package com.obb.online_blackboard.service;

import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.model.RoomModel;
import com.obb.online_blackboard.model.ShapeModel;
import com.obb.online_blackboard.model.SheetModel;
import com.obb.online_blackboard.model.UserModel;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tool.annotation.Lock;
import tool.result.Message;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
    UserModel userModel;

    @Resource
    Id id;

    @Resource
    RedissonClient client;

    /**
     * 权限要求
     * 只读模式：多用于白板完成后展示，每个人都无法编辑，所有人在翻页时会自动同步翻页
     * 协作模式：多用于白板创作阶段，每个人可以在相同或不同的当前页编辑页编辑内容
     * @param userId 用户ID
     * @param roomId 房间号
     * @param sheetId 画布ID
     */
    private void verifyCreator(long userId, long roomId, long sheetId){
        RoomEntity room = roomModel.getVerifyRoom(roomId);
        if(room == null){
            throw new OperationException(404, "房间不存在");
        }
        if(!room.getSheets().contains(sheetId)){
            throw new OperationException(404, "画布不存在");
        }
//        if(room.getNowSheet() != sheetId){
//            throw new OperationException(403, "无法编辑没选中的画布");
//        }
        if(!room.getStatus().equals("meeting")){
            throw new OperationException(403, "会议已结束或者未没开始");
        }
        if(room.getSetting().getIsShare() == 0 /**&& room.getCreatorId() != userId**/){
            throw new OperationException(403, "只读模式无法编辑");
        }
        UserDataEntity user = userModel.getUserById(userId);
        if(user.getIsAnonymous() == 1) {
            throw new OperationException(403, "匿名用户无法编辑");
        }
    }

    public void createSheet(long roomId, String name, long userId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(!room.getStatus().equals("meeting")){
            throw new OperationException(403, "会议已经结束或者还未开始");
        }
        UserDataEntity user = userModel.getUserById(userId);
        if(user.getNowRoom() != roomId){
            throw new OperationException(403, "你不在房间中");
        }
        SheetEntity sheet = sheetModel.createSheet(name, room.getId());
        File file = new File(sheetModel.path + "/sheet-" + sheet.getId() + ".txt");
        try{
            file.createNewFile();
        }catch (IOException e){
            sheetModel.delete(sheet.getId());
            throw new OperationException(500, "创建画布失败");
        }
        room.getSheets().add(sheet.getId());
        roomModel.updateRoom(roomId, "sheets", room.getSheets());
        template.convertAndSend("/exchange/room/" + room.getId(), Message.def("/create_sheet", sheet));
    }

    public void draw(long userId, Shape shape, long roomId, long sheetId){
        verifyCreator(userId, roomId, sheetId);
        long id = this.id.getId("shape");
        shape.setId(id);
        template.convertAndSend("/exchange/room/" + roomId, Message.add(shape, sheetId));
        RLock rLock = client.getLock("SHEET_WRITE_" + sheetId);
        rLock.lock(5, TimeUnit.SECONDS);
        SheetEntity sheet = new SheetEntity(sheetId, roomId);
        shape.setSheetId(sheetId);
        shape.setUsing(true);
        shapeModel.saveShape(shape);
        sheet.addStack(shape.getId());
        rLock.unlock();
        template.convertAndSend("/exchange/room/" + roomId, Message.add(shape, sheetId));
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void rollback(long userId, long roomId, long sheetId){
        verifyCreator(userId, roomId, sheetId);
        SheetEntity sheet = sheetModel.getSheetById(sheetId);
        sheet.rollback();
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void redo(long userId, long roomId, long sheetId) {
        verifyCreator(userId, roomId, sheetId);
        SheetEntity sheet = new SheetEntity(sheetId, roomId);
        sheet.redo();

    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void modify(long userId, Shape shape, long roomId, long sheetId){
        verifyCreator(userId, roomId, sheetId);
        long id = shape.getId();
        shape.setSheetId(sheetId);
        SheetEntity sheet = new SheetEntity(sheetId, roomId);
        if(!shapeModel.getById(shape.getId()).isUsing()){
            throw new OperationException(404, "图像不存在");
        }
        Shape s = shapeModel.createShape(shape);
        Shape s1 = shapeModel.getById(id);
        sheet.modStack(id, s.getId());
        s.setUsing(true);
        s1.setUsing(false);
        shapeModel.saveShape(s);
        shapeModel.saveShape(s1);
        template.convertAndSend("/exchange/room/" + roomId, Message.del(id, sheetId));
        template.convertAndSend("/exchange/room/" + roomId, Message.add(shape, sheetId));
    }

    @Lock(key = "SHEET_WRITE_", argName = "sheetId")
    public void delete(long userId, long roomId, long sheetId, long shapeId){
        verifyCreator(userId, roomId, sheetId);
        Shape s = shapeModel.getById(shapeId);
        if(s == null){
            return;
        }
        SheetEntity sheet = new SheetEntity(sheetId, roomId);
        sheet.delStack(shapeId);
        Shape shape = shapeModel.getById(shapeId);
        shape.setUsing(false);
        shapeModel.saveShape(shape);
        template.convertAndSend("/exchange/room/" + roomId, Message.del(shapeId, sheetId));
    }


    public void updateNowSheet(long roomId, long userId, long sheetId){
        RoomEntity room = roomModel.getRoomById(roomId);
        if(room.getCreatorId() != userId){
            return;
        }
        List<SheetEntity> sheets = sheetModel.getAllByRoomId(roomId);
        sheets.forEach(item -> {
            if(item.getId() == sheetId){
                room.setNowSheet(sheetId);
                roomModel.saveRoom(room);
            }
        });
    }


    public SheetEntity getSheetById(long sheetId,  long roomId, long userId){
        RoomEntity r = roomModel.getRoomById(roomId);
        if(!r.getStatus().equals("meeting")){
            throw new OperationException(500, "会议未开始或已结束");
        }
        if(!r.getSheets().contains(sheetId)){
            throw new OperationException(404, "画布不存在");
        }
        if(r.getCreatorId() == userId){
            r.setNowSheet(sheetId);
            roomModel.saveRoom(r);
        }
        SheetEntity sheet = sheetModel.getSheetById(sheetId);
        return sheet;
    }

    @Lock()
    public void click(){

    }

    public void unclick(){}

}
