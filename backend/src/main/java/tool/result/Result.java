package tool.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author 陈桢梁
 * @desc Result.java
 * @date 2021-12-02 17:14
 * @logs[0] 2021-12-02 17:14 陈桢梁 创建了Result.java文件
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public Result(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result Error(Exception e){
        return new Result(500,e.getLocalizedMessage(),null);
    }

    public static <T> Result success(String msg, T data){
        return new Result(200, msg, data);
    }

    public static Result fail(int code, String msg){
        return new Result(code, msg, null);
    }

    public Result(){

    }

}
