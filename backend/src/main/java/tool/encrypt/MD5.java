package tool.encrypt;

import org.apache.tomcat.util.security.MD5Encoder;

import java.nio.charset.StandardCharsets;

/**
 * @author 陈桢梁
 * @desc MD5.java
 * @date 2022-10-27 11:13
 * @logs[0] 2022-10-27 11:13 陈桢梁 创建了MD5.java文件
 */
public class MD5 {

    private static final String salt = "MD5_SALT";

    public static String salt(String text){
        return MD5Encoder.encode(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String salt(String text, int count){
        text = text + salt;
        for(int i = 0;i < count;i++){
           text = salt(text);
        }
        return text;
    }

}
