package tool.util.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 陈桢梁
 * @desc Message.java
 * @date 2022-11-12 16:56
 * @logs[0] 2022-11-12 16:56 陈桢梁 创建了Message.java文件
 */
@Data
@AllArgsConstructor
public class Message<T>{

    private long id;

    private T data;

}
