package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈桢梁
 * @desc ShapeDap.java
 * @date 2022-10-29 15:47
 * @logs[0] 2022-10-29 15:47 陈桢梁 创建了ShapeDap.java文件
 */
@Repository
public interface ShapeDao extends CrudRepository<Shape, Long>, QueryByExampleExecutor<Shape> {

    
    Shape findShapeById(long id);

    List<Shape> findBySheetId(long sheetId);


}
