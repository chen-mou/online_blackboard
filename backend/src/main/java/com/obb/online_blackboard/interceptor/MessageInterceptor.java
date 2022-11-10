package com.obb.online_blackboard.interceptor;

import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.exception.OperationException;
import com.obb.online_blackboard.service.RoomService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.xerial.snappy.Snappy;
import tool.result.Result;
import tool.util.JWT;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
            RedisTemplate redis = (RedisTemplate) Context.getContext().getBean("redisTemplate");
            Object rt = redis.opsForValue().get("token:" + userId);
            if(rt == null){
                throw new OperationException(403, "登录过时");
            }
            String rtoken = (String) rt;
            if(!rtoken.equals(token)){
                throw new OperationException(403, "登录过时");
            }
            List<String> roomId = accessor.getNativeHeader("Room-Id");
            List<String> isAnonymous = accessor.getNativeHeader("Is-Anonymous");

            if(roomId == null){
                throw new OperationException(500, "房间号不能为空");
            }

            int anonymous = Integer.parseInt(((List<String>)getOrDefault(isAnonymous, new ArrayList<>(){{add("0");}})).get(0));
            accessor.setUser(() -> userId);
            String session = accessor.getSessionId();
            redis.opsForValue().set("session:" + session, userId);
            redis.opsForValue().set("user_session:" + userId, session);
            r.joinRoom(roomId.get(0), Long.parseLong(userId), anonymous);
            return message;
        }
        return message;

    }

    private Object getOrDefault(Object o, Object def){
        if(o == null){
            return def;
        }else{
            return o;
        }
    }

}
