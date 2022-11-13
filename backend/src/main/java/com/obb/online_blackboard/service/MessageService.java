package com.obb.online_blackboard.service;

import com.obb.online_blackboard.entity.FileEntity;
import com.obb.online_blackboard.entity.MessageEntity;
import com.obb.online_blackboard.entity.UserDataEntity;
import com.obb.online_blackboard.model.FileModel;
import com.obb.online_blackboard.model.UserModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tool.result.Message;

import javax.annotation.Resource;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc     MessageService.java
 * @author  陈桢梁
 * @date    2022-11-10 13:16
 * @logs[0] 2022-11-10 13:16 陈桢梁 创建了MessageService.java文件
 */
@Service
public class MessageService {

    @Resource
    UserModel userModel;

    @Resource
    FileModel file;

    @Resource
    SimpMessagingTemplate template;

    public void send(long sender, MessageEntity message){
        message.setSender(sender);
        UserDataEntity senderEntity = userModel.getUserById(sender);
        message.setSenderName(senderEntity.getNickname());
        message.setTime(new Date());
        long roomId = senderEntity.getNowRoom();
        if(message.getType().equals("emoji")){
            String msg = message.getMsg();
            Pattern p = Pattern.compile("\\[file_id=(.*?)]");
            Matcher m = p.matcher(msg);
            FileEntity fileEntity = file.getById(Long.parseLong(m.group(1)));
            message.setMsg("[url=" + fileEntity.getUri() + "]");
        }
        if(message.isBroadcast()){
            template.convertAndSend("/exchange/room/" + roomId, Message.def("message", message));
        }else{
            UserDataEntity getterEntity = userModel.getUserById(message.getGetter());
            message.setGetterName(getterEntity.getNickname());
            template.convertAndSendToUser(String.valueOf(getterEntity.getUserId()),
                    "/queue/info", Message.def("message", message));
        }

    }

}
