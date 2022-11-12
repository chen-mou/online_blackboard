package tool.util.kafka.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import tool.util.kafka.Message;

/**
 * @author 陈桢梁
 * @desc MessageSerializer.java
 * @date 2022-11-12 16:56
 * @logs[0] 2022-11-12 16:56 陈桢梁 创建了MessageSerializer.java文件
 */
public class MessageSerializer implements Serializer<Message> {
    @Override
    public byte[] serialize(String s, Message message) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsBytes(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
