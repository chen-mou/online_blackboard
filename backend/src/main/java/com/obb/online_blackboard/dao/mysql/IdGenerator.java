package com.obb.online_blackboard.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author 陈桢梁
 * @desc KeyGenerator.java
 * @date 2022-10-28 20:41
 * @logs[0] 2022-10-28 20:41 陈桢梁 创建了KeyGenerator.java文件
 */
@Mapper
public interface IdGenerator {

    @Select("select value from redis_model_id where name = #{table}")
    Long getId(String table);

    @Update("update  redis_model_id set value = #{id} where name = #{table}")
    void updateId(String table, long id);

}
