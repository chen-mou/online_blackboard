package com.obb.online_blackboard.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc PrincipalInterceptor.java
 * @date 2022-10-29 10:12
 * @logs[0] 2022-10-29 10:12 陈桢梁 创建了PrincipalInterceptor.java文件
 */
@Component
public class PrincipalInterceptor extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        long userId = (Long)attributes.get("CurrentUserId");
        if(request instanceof ServletServerHttpRequest){
            return () -> String.valueOf(userId);
        }
        return super.determineUser(request, wsHandler, attributes);
    }



}
