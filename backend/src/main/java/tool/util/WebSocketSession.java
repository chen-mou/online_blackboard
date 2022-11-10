package tool.util;

import com.obb.online_blackboard.config.Websocket;
import org.springframework.web.server.WebSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc WebSocketSession.java
 * @date 2022-11-08 15:51
 * @logs[0] 2022-11-08 15:51 陈桢梁 创建了WebSocketSession.java文件
 */
public class WebSocketSession {

    public static ConcurrentWebSocketSessionDecorator getSession(String sessionID) {
        Map sessions = Websocket.sessions;
        Object obj = sessions.get(sessionID);
        if(obj == null){
            return null;
        }
        try {
            Field sf = obj.getClass().getDeclaredField("session");
            sf.setAccessible(true);
            return (ConcurrentWebSocketSessionDecorator) sf.get(obj);
        }catch (NoSuchFieldException e){
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
