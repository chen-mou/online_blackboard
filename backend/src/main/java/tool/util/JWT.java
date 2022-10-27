package tool.util;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.obb.online_blackboard.exception.OperationException;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc JWT.java
 * @date 2022-10-27 14:49
 * @logs[0] 2022-10-27 14:49 陈桢梁 创建了JWT.java文件
 */
public class JWT {

    private static String SECRET = "BLACKBOARD_KEY";

    private static String ISSUER = "BLACKBOARD_ISSUER";

    public static String encode(Map<String, String> date){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = com.auth0.jwt.JWT.create()
                    .withIssuer(ISSUER)
                    //设置过期时间为1天
                    .withExpiresAt(DateUtils.addDays(new Date(), 1));
            date.forEach(builder::withClaim);
            return builder.sign(algorithm);
        } catch (IllegalArgumentException  e) {
            throw new OperationException(500, "生成token失败");
        }
    }

    public static Map<String, String> decode(String token){
        Algorithm algorithm;
        Map<String, Claim> map;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            map = jwt.getClaims();
        } catch (Exception e) {
            throw new OperationException(500, "token有误");
        }
        Map<String, String> resultMap = new HashMap<>(map.size());
        map.forEach((k, v) -> resultMap.put(k, v.asString()));
        return resultMap;
    }

}
