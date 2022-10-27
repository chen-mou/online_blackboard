package tool.util.id;


import java.util.Date;

/**
 * @author 陈桢梁
 * @desc Id.java
 * @date 2022-10-27 14:39
 * @logs[0] 2022-10-27 14:39 陈桢梁 创建了Id.java文件
 */
public class Id {

    public static long getId(String name){
        long base = name.hashCode();
        long machine = System.getenv("COMPUTERNAME").hashCode();
        long now = new Date().getTime();
        return now + base * machine;
    }

}
