package com.obb.online_blackboard.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 陈桢梁
 * @desc KeyGenerator.java
 * @date 2022-10-28 20:41
 * @logs[0] 2022-10-28 20:41 陈桢梁 创建了KeyGenerator.java文件
 */
@Mapper
public interface IdGenerator {

    @Select("select id from ${table} order by id limit 1")
    Long getId(String table);

}
