package com.obb.online_blackboard.entity.operate;

import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.dao.redis.ShapeDao;
import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.entity.base.Save;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.model.ShapeModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tool.result.Message;

import java.util.Optional;
import java.util.Set;

/**
 * @author 陈桢梁
 * @desc Set.java
 * @date 2022-11-01 20:04
 * @logs[0] 2022-11-01 20:04 陈桢梁 创建了Set.java文件
 */
@AllArgsConstructor
@Data
public class Modify implements Operate {

    long from;

    long to;
    @Override
    public void rollback(long sheetId,long roomId) {
        Modify(roomId, from, to, sheetId);
    }

    @Override
    public void redo(long sheetId,long roomId) {
        Modify(roomId, to, from, sheetId);
    }

    private void Modify(long roomId, long to, long from, long sheetId) {
        SimpMessagingTemplate s = Context.getContext().getBean(SimpMessagingTemplate.class);
        s.convertAndSend("/exchange/room/" + roomId, Message.del(to, sheetId));
        ApplicationContext app = Context.getContext();
        ShapeModel shapeModel = app.getBean(ShapeModel.class);;
        Shape shape1 = shapeModel.getById(from);
        Shape shape2 = shapeModel.getById(to);
        shapeModel.saveShape(shape1);
        shapeModel.saveShape(shape2);
        s.convertAndSend("/exchange/room/" + roomId, Message.add(shape1, sheetId));
    }
}
