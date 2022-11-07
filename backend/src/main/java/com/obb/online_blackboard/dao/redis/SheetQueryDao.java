package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.SheetEntity;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc SheetQueryDao.java
 * @date 2022-11-06 16:52
 * @logs[0] 2022-11-06 16:52 陈桢梁 创建了SheetQueryDao.java文件
 */
@Repository
public interface SheetQueryDao extends QueryByExampleExecutor<SheetEntity> {
}
