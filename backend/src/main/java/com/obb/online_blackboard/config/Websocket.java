package com.obb.online_blackboard.config;

import com.obb.online_blackboard.interceptor.HandInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


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

    HandInterceptor handInterceptor;

    @Autowired
    public Websocket(HandInterceptor handInterceptor){
        this.handInterceptor = handInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(handInterceptor);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/queue","/test");
        registry.setUserDestinationPrefix("/user").setApplicationDestinationPrefixes("/app");

    }
}
