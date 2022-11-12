package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.Shapes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc ShapesDao.java
 * @date 2022-11-13 01:44
 * @logs[0] 2022-11-13 01:44 陈桢梁 创建了ShapesDao.java文件
 */
@Repository
public interface ShapesDao extends CrudRepository<Shapes, Long> {
}
