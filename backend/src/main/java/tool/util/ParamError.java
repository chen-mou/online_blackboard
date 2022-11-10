package tool.util;

import com.obb.online_blackboard.exception.OperationException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * @author 陈桢梁
 * @desc ParamError.java
 * @date 2022-10-27 15:16
 * @logs[0] 2022-10-27 15:16 陈桢梁 创建了ParamError.java文件
 */
public class ParamError {

    public static void handlerError(Errors errors){
        if(errors.hasErrors()){
            StringBuilder msg = new StringBuilder();
            for(ObjectError e : errors.getAllErrors()){
                msg.append(e.getDefaultMessage() + "\n");
            }
            throw new OperationException(500, msg.toString());
        }
    }

}
