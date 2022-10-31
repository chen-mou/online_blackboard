package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.IdGenerator;
import com.obb.online_blackboard.dao.redis.ShapeDao;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc ShapeModel.java
 * @date 2022-10-31 18:50
 * @logs[0] 2022-10-31 18:50 陈桢梁 创建了ShapeModel.java文件
 */
@Repository
public class ShapeModel {

    @Resource
    ShapeDao shapeDao;

    @Resource
    Id id;

    public Shape createShape(Shape shape){
        long id = this.id.getId("shape");
        shape.setId(id);
        shapeDao.save(shape);
        return shape;
    }

    public void saveShape(Shape shape){
        shapeDao.save(shape);
    }

    public List<Shape> getShapeBySheetId(long sheetId){
        return shapeDao.getShapesBySheetId(sheetId);
    }

}
