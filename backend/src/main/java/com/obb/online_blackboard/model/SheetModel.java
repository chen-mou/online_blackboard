package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.redis.SheetDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 陈桢梁
 * @desc SheetModel.java
 * @date 2022-10-27 19:55
 * @logs[0] 2022-10-27 19:55 陈桢梁 创建了SheetModel.java文件
 */
@Repository
public class SheetModel {

    @Resource
    SheetDao sheetDao;

//    @Resource
//    SheetIdDao sheetIdDao;

    @Resource
    ShapeModel shapeModel;

    @Resource
    RedisKeyValueTemplate template;
    @Resource
    Id id;

    public final String path = "./file/report";

    public SheetEntity createSheet(String name, long roomId){
        SheetEntity sheetEntity = new SheetEntity(id.getId("sheet"), roomId);
        sheetEntity.setName(name);
//        sheetIdDao.updateId(sheetEntity.getId());
        sheetDao.save(sheetEntity);
        return sheetEntity;
    }

    public void save(SheetEntity sheetEntity){
        sheetDao.save(sheetEntity);
    }

    public void updateSheet(long sheetId, String key, Object val){
        PartialUpdate<SheetEntity> update = new PartialUpdate<>(sheetId, SheetEntity.class).set(key, val);
        template.update(update);
    }

    public SheetEntity getSheetById(long id){
        SheetEntity sheet = getSheetByIdBase(id);
        if(sheet == null){
            return null;
        }
        List<Shape> shapes = shapeModel.getBySheetId(sheet.getId());
        sheet.setShapeEntities(shapes);
//        sheet.setUseStack(null);
//        sheet.setUserIndex(null);
        return sheet;
    }

    public SheetEntity getSheetByIdBase(long id){
        Optional<SheetEntity> optional = sheetDao.findById(id);
        if(optional.isEmpty()){
            return null;
        }
        SheetEntity sheet = optional.get();
        return sheet;
    }

    public List<SheetEntity> getSheetsById(List<Long> sheetsId){
        List<SheetEntity> res = new ArrayList<>();
        sheetsId.forEach((item) -> {
            Optional<SheetEntity> sheet = sheetDao.findById(item);
            if(!sheet.isEmpty()){
                SheetEntity val = sheet.get();
                SheetEntity sheetEntity = new SheetEntity(val.getId(), val.getRoomId());
                sheetEntity.setName(val.getName());
                res.add(sheetEntity);
            }
        });
        return res;
    }

    public List<SheetEntity> getAllByRoomId(long roomId){
        List<SheetEntity> sheetEntities = sheetDao.findAllByRoomId(roomId);
        return sheetEntities;
    }

    public void delete(long sheetId){
        sheetDao.deleteById(sheetId);
    }

//    public Iterable<SheetEntity> getAllSheetName(){
//
//    }

}
