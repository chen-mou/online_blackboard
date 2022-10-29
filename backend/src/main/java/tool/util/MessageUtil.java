package tool.util;

import com.obb.online_blackboard.dao.redis.RoomDao;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc MessageUtil.java
 * @date 2022-10-29 10:36
 * @logs[0] 2022-10-29 10:36 陈桢梁 创建了MessageUtil.java文件
 */
@Component
public class MessageUtil {

    @Resource
    RoomDao roomDao;

    @Resource
    SimpMessagingTemplate template;

    public void sendParticipants(String roomID, String des, Object payload){
        RoomEntity room = roomDao.getRoomEntityById(roomID);
        room.getParticipants().forEach((item) -> template.convertAndSendToUser(String.valueOf(item.getUserId()), des, payload));
    }

    public void sendParticipants(List<UserDataEntity> participants, String des, Object payload){
       participants.forEach((item) -> template.convertAndSendToUser(String.valueOf(item.getUserId()), des, payload));
    }

}
