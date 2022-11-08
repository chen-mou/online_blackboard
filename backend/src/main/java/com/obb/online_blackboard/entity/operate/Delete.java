package com.obb.online_blackboard.entity.operate;

import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.dao.redis.ShapeDao;
import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.entity.base.Save;
import com.obb.online_blackboard.entity.base.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tool.result.Message;

import java.util.Optional;
import java.util.Set;

/**
 * @author 陈桢梁
 * @desc Delete.java
 * @date 2022-11-01 20:04
 * @logs[0] 2022-11-01 20:04 陈桢梁 创建了Delete.java文件
 */
@AllArgsConstructor
@Data
public class Delete implements Operate {

    long shapeId;
    @Override
    public void rollback(Set<Long> shapes, long sheetId,String roomId, Save save) {
        shapes.add(shapeId);
        ApplicationContext app = Context.getContext();
        SimpMessagingTemplate s = app.getBean(SimpMessagingTemplate.class);
        ShapeDao shapeDao = app.getBean(ShapeDao.class);
        Optional<Shape> shape = shapeDao.findById(shapeId);
        save.save();
        s.convertAndSend("/exchange/room/" + roomId, Message.add(shape.get(), sheetId));
    }

    @Override
    public void redo(Set<Long> shapes, long sheetId, String roomId, Save save) {
        shapes.remove(shapeId);
        SimpMessagingTemplate s = Context.getContext().getBean(SimpMessagingTemplate.class);
        save.save();
        s.convertAndSend("/exchange/room/" + roomId, Message.add(shapeId, sheetId));
    }
}