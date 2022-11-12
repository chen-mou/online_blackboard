package com.obb.online_blackboard.config;

import com.obb.online_blackboard.handler.JsonMessageHandler;
import com.obb.online_blackboard.handler.ShapeHandler;
import com.obb.online_blackboard.interceptor.MessageInterceptor;
import com.obb.online_blackboard.interceptor.PrincipalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
import tool.result.Message;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 陈桢梁
 * @desc Websocket.java
 * @date 2021-12-04 21:22
 * @logs[0] 2021-12-04 21:22 陈桢梁 创建了Websocket.java文件
 */
@Slf4j
@Component
@EnableWebSocket
@EnableWebSocketMessageBroker
public class Websocket implements WebSocketMessageBrokerConfigurer {


    PrincipalInterceptor principalInterceptor;

    public static Map sessions;

    @Autowired
    public Websocket(PrincipalInterceptor principalInterceptor){
        this.principalInterceptor = principalInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOriginPatterns("*")
//                .addInterceptors(new HandInterceptor())
//                .setHandshakeHandler(principalInterceptor)
                .withSockJS();
        if(registry instanceof WebMvcStompEndpointRegistry){
            try {
                Field field = registry.getClass().getDeclaredField("subProtocolWebSocketHandler");
                field.setAccessible(true);
                SubProtocolWebSocketHandler sub = (SubProtocolWebSocketHandler) field.get(registry);
                Field session = sub.getClass().getDeclaredField("sessions");
                session.setAccessible(true);
                Map sessions = (Map) session.get(sub);
                this.sessions = sessions;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MessageInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        WebSocketMessageBrokerConfigurer.super.addArgumentResolvers(argumentResolvers);

        /*注意这个顺序和执行顺序要是一致的*/

        argumentResolvers.add(new JsonMessageHandler());
        argumentResolvers.add(new ShapeHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
//        registry.enableSimpleBroker("/room");
        registry.setUserDestinationPrefix("/user").setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/exchange", "/queue")
                .setRelayHost("47.112.184.57")   //地址
                .setRelayPort(61613)     //端口
                .setClientLogin("czl")  // 账号密码
                .setClientPasscode("CZLczl@20010821")
                .setSystemLogin("czl")
                .setSystemPasscode("CZLczl@20010821")
                .setVirtualHost("/websocket");

    }
    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        ApplicationContext app = Context.getContext();
        RedisTemplate<String, Object> redis = (RedisTemplate<String, Object>) app.getBean("redisTemplate");
        SimpMessagingTemplate template = app.getBean(SimpMessagingTemplate.class);
        Object userId = redis.opsForValue().get("session:" + sha.getSessionId());

        redis.delete("session:" + sha.getSessionId());
        redis.delete("user_session:" + userId);
        template.convertAndSend("/exchange/room/", Message.def("change_status", new HashMap<>(){
            {
                put("userId", userId);
                put("status", "offline");
            }
        }));
    }

}
