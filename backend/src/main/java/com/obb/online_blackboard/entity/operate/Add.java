package com.obb.online_blackboard.entity.operate;

import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.entity.base.Operate;
import com.obb.online_blackboard.entity.base.Save;
import com.obb.online_blackboard.entity.base.Shape;
import com.obb.online_blackboard.model.ShapeModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tool.result.Message;

import java.util.Set;

/**
 * @author 陈桢梁
 * @desc Add.java
 * @date 2022-11-01 20:04
 * @logs[0] 2022-11-01 20:04 陈桢梁 创建了Add.java文件
 */
@Data
@AllArgsConstructor
public class Add implements Operate {

    private long shapeId;

    @Override
    public void rollback(long sheetId,long roomId) {
        ApplicationContext app = Context.getContext();
        SimpMessagingTemplate s = app.getBean(SimpMessagingTemplate.class);

        ShapeModel shapeModel = app.getBean(ShapeModel.class);
        Shape shape = shapeModel.getById(shapeId);
        shape.setUsing(false);
        shapeModel.saveShape(shape);
        /**
         * 应该要保存了再将消息发送出去，其他地方同理
         */
        s.convertAndSend("/exchange/room/" + roomId, Message.del(shapeId, sheetId));
    }

    @Override
    public void redo(long sheetId,long roomId) {
        ApplicationContext app = Context.getContext();
        SimpMessagingTemplate s = app.getBean(SimpMessagingTemplate.class);
        ShapeModel shapeDao = app.getBean(ShapeModel.class);
        Shape shape = shapeDao.getById(shapeId);
        shape.setUsing(true);
        shapeDao.saveShape(shape);
        s.convertAndSend("/exchange/room/" + roomId, Message.add(shape, sheetId));
    }
}
