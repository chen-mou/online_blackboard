package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.base.Operate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc OperateDao.java
 * @date 2022-11-03 19:00
 * @logs[0] 2022-11-03 19:00 陈桢梁 创建了OperateDao.java文件
 */
@Repository
public interface OperateDao extends CrudRepository<Operate, Long> {
}
