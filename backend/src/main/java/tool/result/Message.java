package tool.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.exception.OperationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.kafka.core.KafkaTemplate;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author 陈桢梁
 * @desc Message.java
 * @date 2022-10-31 21:32
 * @logs[0] 2022-10-31 21:32 陈桢梁 创建了Message.java文件
 */
@AllArgsConstructor
@Data
public class Message<T> {

    String type;

    long sheet;

    boolean isZip;

    String data;

    public Message(String type, T date) {
        try {
            this.type = type;
            String str = new ObjectMapper().writeValueAsString(date);
//            if(str.length() > 1000){
//                str = new String(Snappy.compress(str, StandardCharsets.UTF_8));
//                this.isZip = true;
//            }
            this.data = str;
        }catch (JsonProcessingException e){
            throw new OperationException(500, "消息序列化出错");
        }catch (IOException e){
            throw new OperationException(500, "消息压缩出错");
        }
    }

    public static <T> Message def(String type, T date){
        return new Message(type, date);
    }

    public static <T> Message del(T date, long sheet){
        Message msg = def("delete", date);
        msg.setSheet(sheet);
//        KafkaTemplate temp = Context.getContext().getBean(KafkaTemplate.class);
//        temp.send("report", new tool.util.kafka.Message(new Date().getTime(), msg));
        return msg;
    }

    public static <T> Message mod(T date){

        return def("modify", date);
    }

    public static <T> Message add(T date, long sheet) {
        Message msg = def("add", date);
        msg.setSheet(sheet);
//        KafkaTemplate temp = Context.getContext().getBean(KafkaTemplate.class);
//        temp.send("report", new tool.util.kafka.Message(new Date().getTime(), msg));
        return msg;
    }

}
