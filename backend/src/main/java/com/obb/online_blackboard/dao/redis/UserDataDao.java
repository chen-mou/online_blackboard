package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.UserDataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 陈桢梁
 * @desc UserDateDao.java
 * @date 2022-11-07 17:31
 * @logs[0] 2022-11-07 17:31 陈桢梁 创建了UserDateDao.java文件
 */
@Repository
public interface UserDataDao extends QueryByExampleExecutor<UserDataEntity>,CrudRepository<UserDataEntity, Long> {
}
