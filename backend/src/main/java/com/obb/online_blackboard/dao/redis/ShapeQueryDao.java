package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc ShapeQueryDao.java
 * @date 2022-11-06 16:40
 * @logs[0] 2022-11-06 16:40 陈桢梁 创建了ShapeQueryDao.java文件
 */
@Repository
public interface ShapeQueryDao extends QueryByExampleExecutor<Shape> {
}
