package com.obb.online_blackboard.convert;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.obb.online_blackboard.entity.base.Shape;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.core.convert.DefaultRedisTypeMapper;

import java.util.Map;

/**
 * @author 陈桢梁
 * @desc ShapeConvert.java
 * @date 2022-10-29 16:01
 * @logs[0] 2022-10-29 16:01 陈桢梁 创建了ShapeConvert.java文件
 */
@ReadingConverter
public class ShapeConvert extends DefaultRedisTypeMapper {


}
