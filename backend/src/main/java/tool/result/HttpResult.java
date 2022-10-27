package tool.result;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author 陈桢梁
 * @desc HttpResult.java
 * @date 2022-01-29 23:37
 * @logs[0] 2022-01-29 23:37 陈桢梁 创建了HttpResult.java文件
 */
public class HttpResult {

    public static Map<String, Object> getDate(String httpText){
        Map result = (Map)JSON.parse(httpText);
        return (Map)JSON.parse(result.get("data").toString());
    }

}
