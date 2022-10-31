package tool.result;

import lombok.AllArgsConstructor;

/**
 * @author 陈桢梁
 * @desc Message.java
 * @date 2022-10-31 21:32
 * @logs[0] 2022-10-31 21:32 陈桢梁 创建了Message.java文件
 */
@AllArgsConstructor
public class Message<T> {

    String type;

    T date;

    public static <T> Message def(String type, T date){
        return new Message(type, date);
    }

    public static <T> Message del(T date){
        return def("delete", date);
    }

    public static <T> Message add(T date){
        return def("add", date);
    }

}
