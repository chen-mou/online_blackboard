package tool.util;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.exception.OperationException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈桢梁
 * @desc JWT.java
 * @date 2022-10-27 14:49
 * @logs[0] 2022-10-27 14:49 陈桢梁 创建了JWT.java文件
 */
public class JWT {

    private static String SECRET = "BLACKBOARD_KEY";

    private static String ISSUER = "BLACKBOARD_ISSUER";

    /**
     * 获取token
     * @param date token中的数据必须要有userId
     * @return
     */
    public static String encode(Map<String, String> date){
        String token = encode(date, ISSUER, SECRET);
        RedisTemplate redis = (RedisTemplate) Context.getContext().getBean("redisTemplate");
        redis.opsForValue().set("token:" + date.get("userId"), token, 1, TimeUnit.DAYS);
        return token;
    }

    public static String encode(Map<String, String> date, String issuer, String secret){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = com.auth0.jwt.JWT.create()
                    .withIssuer(issuer)
                    //设置过期时间为1天
                    .withExpiresAt(DateUtils.addDays(new Date(), 1));
            date.forEach(builder::withClaim);

            String token = builder.sign(algorithm);
            return token;
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
