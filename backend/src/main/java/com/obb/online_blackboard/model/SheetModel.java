package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.SheetIdDao;
import com.obb.online_blackboard.dao.redis.SheetDao;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.*;

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
    Id id;

    public SheetEntity createSheet(String name){
        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setId(id.getId("sheet"));
        sheetEntity.setName(name);
//        sheetIdDao.updateId(sheetEntity.getId());
        sheetDao.save(sheetEntity);
        return sheetEntity;
    }

    public void save(SheetEntity sheetEntity){
        sheetDao.save(sheetEntity);
    }

    public SheetEntity getSheetById(long id){
        Optional<SheetEntity> optional = sheetDao.findById(id);
        if(optional.isEmpty()){
            return null;
        }
        SheetEntity sheet = optional.get();
        List<Shape> shapes = shapeModel.getShapeBySheetId(sheet.getId());
        sheet.setShapeEntities(shapes);
//        sheet.setUseStack(null);
//        sheet.setUserIndex(null);
        return sheet;
    }

    public List<SheetEntity> getSheetsByRoomId(String roomId){
        return sheetDao.findSheetEntitiesByRoomId(roomId);
    }

}
