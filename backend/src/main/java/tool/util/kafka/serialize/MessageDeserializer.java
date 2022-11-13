package tool.util.kafka.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import tool.util.kafka.Message;

import java.io.IOException;

/**
 * @author 陈桢梁
 * @desc MessageDeserializer.java
 * @date 2022-11-12 16:56
 * @logs[0] 2022-11-12 16:56 陈桢梁 创建了MessageDeserializer.java文件
 */
public class MessageDeserializer implements Deserializer<Message> {
    @Override
    public Message deserialize(String s, byte[] bytes) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(bytes, Message.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
