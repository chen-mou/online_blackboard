package com.obb.online_blackboard.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.entity.RoomEntity;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.service.RoomService;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import tool.result.Result;
import tool.util.JWT;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc HeadInterceptor.java
 * @date 2021-12-06 20:45
 * @logs[0] 2021-12-06 20:45 陈桢梁 创建了HeadInterceptor.java文件
 */
public class MessageInterceptor implements ChannelInterceptor {


    private void sendError(MessageHeaders headers, MessageChannel channel, int code, String msg){
        channel.send(new Message<>() {
            @Override
            public Result getPayload() {
                return Result.fail(code, msg);
            }

            @Override
            public MessageHeaders getHeaders() {
                return new MessageHeaders(new HashMap<>() {
                    {
                        headers.forEach((key, value) -> {
                            if (key.equals("simpMessageType")) {
                                put(key, SimpMessageType.CONNECT_ACK);
                                return;
                            }
                            if (key.equals("stompCommand")) {
                                put(key, StompCommand.ERROR);
                                return;
                            }
                            put(key, value);
                        });
                    }
                });
            }
        });
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> tokens = accessor.getNativeHeader("Authorization");
            RoomService r = Context.getContext().getBean(RoomService.class);
            if (tokens == null) {
//                sendError(accessor.getMessageHeaders(), channel, 403, "未登录");
                throw new OperationException(403, "未登录");
            }
            String token = tokens.get(0);
            Map<String, String> data = JWT.decode(token);
            if (!data.containsKey("userId")) {
//                sendError(accessor.getMessageHeaders(), channel, 403, "token有误");
                throw new OperationException(403, "token有误");
            }
            String userId = data.get("userId");
            List<String> roomId = accessor.getNativeHeader("Room-Id");
            List<String> isAnonymous = accessor.getNativeHeader("Is-Anonymous");
            accessor.setUser(() -> userId);
            r.joinRoom(roomId.get(0), Long.parseLong(userId), Integer.parseInt(isAnonymous.get(0)));
            return message;
        }
        return message;

    }


}
