package com.obb.online_blackboard.dao.redis;

import com.obb.online_blackboard.entity.SheetEntity;
import com.obb.online_blackboard.model.SheetModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈桢梁
 * @desc SheetDao.java
 * @date 2022-10-27 17:17
 * @logs[0] 2022-10-27 17:17 陈桢梁 创建了SheetDao.java文件
 */
@Repository
public interface SheetDao extends CrudRepository<SheetEntity, Long> {

    List<SheetEntity> getSheetEntitiesByIdIn(List<Long> id);

    List<SheetEntity> findSheetEntitiesByRoomId(String roomId);

    List<SheetEntity> findAllByRoomId(String roomId);
}
