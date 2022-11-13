package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.redis.ShapeDao;
import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Resource
    RedisKeyValueTemplate redisKeyValueTemplate;

    @Resource
    RedisKeyValueTemplate template;
    public Shape createShape(Shape shape){
        long id = this.id.getId("shape");
        shape.setId(id);
        shapeDao.save(shape);
        return shape;
    }

    public void saveShape(Shape shape){
        shapeDao.save(shape);
    }

    public List<Shape> getBySheetId(long sheetId){
        return shapeDao.findBySheetIdAndUsing(sheetId, 1);
    }

    public List<Shape> getShapeByShapesId(List<Long> shapesId){
        List<Shape> res = new ArrayList<>();
        shapesId.forEach((item) -> {
            Optional<Shape> s = shapeDao.findById(item);
            if(!s.isEmpty()){
                res.add(s.get());
            }
        });
        return res;
    }

    public Shape getById(long shapeId){
        Optional<Shape> s = shapeDao.findById(shapeId);
        if(s.isEmpty()){
            return null;
        }
        return s.get();
    }

    public void delete(long shapeId){
        shapeDao.deleteById(shapeId);
    }

    public void update(long shapeId, Map<String, Object> values){
        PartialUpdate<SheetEntity> update = new PartialUpdate<>(shapeId, SheetEntity.class);
        values.forEach((key, value) -> {
            update.set(key, value);
        });
        template.update(update);
    }


    public void updateUsing(boolean using, long shapeId){
        PartialUpdate<Shape> update = new PartialUpdate<>(shapeId, Shape.class).
                set("using", using ? 0: 1);
        redisKeyValueTemplate.update(update);
    }
}
