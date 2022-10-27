package com.obb.online_blackboard.exception;

/**
 * @author 陈桢梁
 * @desc OperationException.java
 * @date 2022-10-27 14:12
 * @logs[0] 2022-10-27 14:12 陈桢梁 创建了OperationException.java文件
 */
public class OperationException extends RuntimeException{

    public OperationException(int code, String msg){
        super("error code : " + code + "," + msg);
    }

}
