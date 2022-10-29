package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc ShapeDap.java
 * @date 2022-10-29 15:47
 * @logs[0] 2022-10-29 15:47 陈桢梁 创建了ShapeDap.java文件
 */
@Repository
public interface ShapeDao extends CrudRepository<Shape, Long> {

    
    Shape getShapeById(long id);

}
