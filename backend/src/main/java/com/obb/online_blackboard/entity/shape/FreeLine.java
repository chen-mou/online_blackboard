package com.obb.online_blackboard.entity.shape;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.entity.base.Point;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.exception.OperationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc FreeLine.java
 * @date 2022-11-11 21:22
 * @logs[0] 2022-11-11 21:22 陈桢梁 创建了FreeLine.java文件
 */
@Data
@NoArgsConstructor
public class FreeLine extends Shape {

    List<Point> data;

    public FreeLine(Map<String, Object> map)  {
        super(map);
        List list = (List) map.get("data");
        data = new ArrayList<>();
        list.forEach((item) -> {
            ObjectMapper om = new ObjectMapper();
            try {
                data.add(om.readValue(om.writeValueAsString(item), Point.class));
            } catch (JsonProcessingException e) {
                throw new OperationException(500, "json转换出错");
            }
        });
    }


    @Override
    public Object handler(Map<String, Object> obj) {
        return new FreeLine(obj);
    }

}
