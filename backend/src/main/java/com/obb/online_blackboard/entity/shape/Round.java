package com.obb.online_blackboard.entity.shape;

import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc Round.java
 * @date 2022-10-29 16:23
 * @logs[0] 2022-10-29 16:23 陈桢梁 创建了Round.java文件
 */
@Data
@NoArgsConstructor
public class Round extends Shape {



    public Round(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Object handler(Map<String, Object> obj) {
        return new Round(obj);
    }
}
