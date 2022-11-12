package com.obb.online_blackboard.listener;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tool.util.kafka.Message;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈桢梁
 * @desc ReportListener.java
 * @date 2022-11-12 19:45
 * @logs[0] 2022-11-12 19:45 陈桢梁 创建了ReportListener.java文件
 */
//@Component
public class ReportListener {

    private final String path = "./file/report";;

    @Resource
    RedissonClient redissonClient;


    @KafkaListener(topics = {"report"})
    public void listen(Message<tool.result.Message> msg) throws IOException {
        tool.result.Message message = msg.getData();
        RLock lock  = redissonClient.getLock("FILE_LOCK:sheet:" + message.getSheet());
        lock.lock(5, TimeUnit.SECONDS);
        FileOutputStream fos = new FileOutputStream(path + "/sheet:" + message.getSheet() + ".txt", true);
        fos.write((message.getData() + "\n").getBytes(StandardCharsets.UTF_8));
        lock.unlock();
    }

}
