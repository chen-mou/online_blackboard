package tool.encrypt;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
/**
 * @author 陈桢梁
 * @desc AES.java
 * @date 2022-10-27 11:14
 * @logs[0] 2022-10-27 11:14 陈桢梁 创建了AES.java文件
 */
public class AES {

    private final static String key = "uUXsN6okXYqsh0BB";

    public static String encrypt(String text, String salt){
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal((text + "|" + salt).getBytes(StandardCharsets.UTF_8));

            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String text){
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(text);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
