package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.redis.SheetDao;
import com.obb.online_blackboard.entity.SheetEntity;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;

/**
 * @author 陈桢梁
 * @desc SheetModel.java
 * @date 2022-10-27 19:55
 * @logs[0] 2022-10-27 19:55 陈桢梁 创建了SheetModel.java文件
 */
@Repository
public class SheetModel {

    @Resource
    SheetDao sheetDao;

    @Resource
    Id id;

    public long createSheet(String name){
        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setId(id.getId("sheet"));
        sheetEntity.setName(name);
        sheetDao.save(sheetEntity);
        return sheetEntity.getId();
    }

}
