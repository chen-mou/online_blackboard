package com.obb.online_blackboard.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author 陈桢梁
 * @desc SheetIdDao.java
 * @date 2022-10-29 16:54
 * @logs[0] 2022-10-29 16:54 陈桢梁 创建了SheetIdDao.java文件
 */
@Mapper
public interface SheetIdDao {
    @Update("update sheet set id = #{id}")
    void updateId(long id);
}
