package com.obb.online_blackboard.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.service.RoomService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import tool.result.Result;
import tool.util.JWT;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc HeadInterceptor.java
 * @date 2021-12-06 20:45
 * @logs[0] 2021-12-06 20:45 陈桢梁 创建了HeadInterceptor.java文件
 */
@Component
public class HandInterceptor implements HandshakeInterceptor {

    private ApplicationContext app = Context.getContext();

    private void authBan(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(403);
        response.getOutputStream().write(Result
                .fail(403, msg)
                .toString()
                .getBytes(StandardCharsets
                        .UTF_8));
    }

    private long vif(HttpHeaders header){
        List<String> tokens = header.get("token");
        if(tokens == null){
            return -1;
        }
        String token = tokens.get(0);
        Map<String, String> data = JWT.decode(token);
        if(!data.containsKey("userId")){
            return -1;
        }
        return Long.parseLong(data.get("userId"));
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        RoomService r = app.getBean(RoomService.class);
        HttpHeaders header = request.getHeaders();
        long userId = vif(header);
        List<String> roomId = header.get("Room-Id");
        List<String> isAnonymous = header.get("Is-Anonymous");
        if(userId == -1){
            authBan((HttpServletResponse) response, "token有误");
            return false;
        }
        RoomEntity room = r.joinRoom(roomId.get(0), userId, Integer.parseInt(isAnonymous.get(0)));
        ObjectMapper om = new ObjectMapper();
        attributes.put("CurrentUser", userId);
        response.getBody().write(om.writeValueAsBytes(room));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }


}
