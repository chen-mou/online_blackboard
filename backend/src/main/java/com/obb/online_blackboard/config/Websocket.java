package com.obb.online_blackboard.config;

import com.obb.online_blackboard.handler.JsonKeyHandler;
import com.obb.online_blackboard.handler.JsonMessageHandler;
import com.obb.online_blackboard.handler.ShapeHandler;
import com.obb.online_blackboard.interceptor.HandInterceptor;
import com.obb.online_blackboard.interceptor.MessageInterceptor;
import com.obb.online_blackboard.interceptor.PrincipalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Stack;


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
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MessageInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        WebSocketMessageBrokerConfigurer.super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new ShapeHandler());
        argumentResolvers.add(new JsonMessageHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/room");
        registry.setUserDestinationPrefix("/user").setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/topic/","/queue/","exchange")
                .setRelayHost("47.112.184.57")   //地址
                .setRelayPort(61613)     //端口
                .setClientLogin("czl")  // 账号密码
                .setClientPasscode("CZLczl@20010821")
                .setSystemLogin("czl")
                .setSystemPasscode("CZLczl@20010821")
                .setVirtualHost("/websocket");

    }


}
