package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.mysql.IdGenerator;
import com.obb.online_blackboard.dao.redis.ShapeDao;
import com.obb.online_blackboard.dao.redis.ShapeQueryDao;
import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

//    @Resource
//    ShapeQueryDao shapeQuery;

    public Shape createShape(Shape shape){
        long id = this.id.getId("shape");
        shape.setId(id);
        shapeDao.save(shape);
        return shape;
    }

    public void saveShape(Shape shape){
        shapeDao.save(shape);
    }

//    public Iterable<Shape> getShapeBySheetId(long sheetId){
//        Shape s = new Shape();
//        s.setSheetId(sheetId);
//        Example<Shape> example = Example.of(s);
//        Iterable<Shape> shapes = shapeQuery.findAll(example);
//        return shapes;
//    }

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

}
