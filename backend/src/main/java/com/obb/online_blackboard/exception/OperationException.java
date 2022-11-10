package com.obb.online_blackboard.exception;

import lombok.Data;

/**
 * @author 陈桢梁
 * @desc OperationException.java
 * @date 2022-10-27 14:12
 * @logs[0] 2022-10-27 14:12 陈桢梁 创建了OperationException.java文件
 */
@Data
public class OperationException extends RuntimeException{

    private int code;

    private String msg;

    public OperationException(int code, String msg){
        super("error code : " + code + "," + msg);
        this.code = code;
        this.msg = msg;
    }

}
