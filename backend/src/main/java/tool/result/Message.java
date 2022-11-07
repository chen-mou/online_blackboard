package tool.result;

import lombok.AllArgsConstructor;
import lombok.Data;

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

    T date;

    public Message(String type, T date){
        this.type = type;
        this.date = date;
    }

    public static <T> Message def(String type, T date){
        return new Message(type, date);
    }

    public static <T> Message del(T date, long sheet){
        Message msg = def("delete", date);
        msg.setSheet(sheet);
        return msg;
    }

    public static <T> Message mod(T date){

        return def("modify", date);
    }

    public static <T> Message add(T date, long sheet){
        Message msg = def("add", date);
        msg.setSheet(sheet);
        return msg;
    }

}
