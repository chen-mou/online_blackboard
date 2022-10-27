package com.obb.online_blackboard.interceptor;

import com.obb.online_blackboard.config.Context;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

/**
 * @author 陈桢梁
 * @desc HeadInterceptor.java
 * @date 2021-12-06 20:45
 * @logs[0] 2021-12-06 20:45 陈桢梁 创建了HeadInterceptor.java文件
 */
@Component
public class HandInterceptor implements ChannelInterceptor {

    private ApplicationContext app = Context.getContext();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())){
            List<String> nativeHeader = accessor.getNativeHeader("Authorization");
            if(nativeHeader != null && !nativeHeader.isEmpty()){
                String token = nativeHeader.get(0);

                return null;
            }
            return null;
        }
        return message;
    }

    private void saveRedis(Principal p){

    }
}
