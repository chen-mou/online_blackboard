package com.obb.online_blackboard.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obb.online_blackboard.config.Context;
import com.obb.online_blackboard.dao.mysql.UserDao;
import com.obb.online_blackboard.entity.UserEntity;
import com.obb.online_blackboard.model.UserModel;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import tool.annotation.NotNeedLogin;
import tool.annotation.Permission;
import tool.result.Result;
import tool.util.JWT;
import tool.util.lock.LockUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author 陈桢梁
 * @desc LoginInterceptor.java
 * @date 2021-12-02 16:50
 * @logs[0] 2021-12-02 16:50 陈桢梁 创建了LoginInterceptor.java文件
 */
public class LoginInterceptor implements HandlerInterceptor {

    private ApplicationContext app = Context.getContext();

    private void authBan(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(403);
        response.getOutputStream().write(Result
                .fail(403, msg)
                .toString()
                .getBytes(StandardCharsets
                        .UTF_8));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
       if(handler instanceof HandlerMethod){
            HandlerMethod methodHandle = (HandlerMethod) handler;
            NotNeedLogin notNeedLogin = methodHandle.getMethodAnnotation(NotNeedLogin.class);
            if(notNeedLogin != null /**|| permission == null**/){
                return true;
            }
            if(token == null){
                authBan(response, "未登录");
                return false;
            } else{
                Map<String, String> data = JWT.decode(token);
                long userId = Long.parseLong(data.get("userId"));
                LockUtil<UserEntity> lockUtil = app.getBean(LockUtil.class);
                UserEntity user = lockUtil.getLock(UserModel.USER_KEY + userId,
                        UserModel.LOCK_KEY + userId,
                        () -> app.getBean(UserDao.class).getById(userId), 3);
                if(user == null){
                    return false;
                }
                request.setAttribute("CurrentUser", user);
//                if(permission.type().equals("auth")){
//                    String auth = permission.auth();
//                    String scope = permission.scope();
//                    if(auth == null || scope == null){
//                        throw new RuntimeException("所选验证方式有误");
//                    }
//                    return authVerify(token, auth, scope, request, response);
//                }else if(permission.type().equals("role")){
//                    String roleName = permission.role();
//                    if(roleName == null || roleName.isEmpty()){
//                        throw new RuntimeException("所选验证方式有误");
//                    }
//                    return roleVerify(token, roleName, request, response);
//                }else{
//                    throw new RuntimeException("所选验证方式有误");
//                }

            }
        }
        return true;
    }

//    private boolean authVerify(String token, String auth, String scope,HttpServletRequest request,HttpServletResponse response) throws Exception {
//        NginxClient client = app.getBean(NginxClient.class);
//        String result = client.verifyAuth(new Auth(token, auth, scope));
//        Map<String, Object> map = (Map<String, Object>) JSON.parse(result);
//        if((Integer) map.get("code") != 200){
//            authBan(response, (String) map.get("msg"));
//            return false;
//        }
//        setRequest(token, request, client);
//        return true;
//    }
//
//    private boolean roleVerify(String token, String roleName, HttpServletRequest request,HttpServletResponse response) throws Exception {
//        NginxClient client = app.getBean(NginxClient.class);
//        String result = client.verify(new Role(token, roleName));
//        ObjectMapper om = new ObjectMapper();
//        Result re = om.readValue(result, Result.class);
//        if(re.getCode() != 200){
//            authBan(response, re.getMsg());
//            return false;
//        }
//        setRequest(token, request, client);
//        return true;
//    }
//
//    private void setRequest(String token, HttpServletRequest request, NginxClient client) throws Exception {
//        app.getBean(Template.class).setToken(token);
//        Map map = HttpResult.getDate(client.getUserInfo());
//        UserBaseEntity user = new UserBaseEntity();
//        user.setId((Integer) map.get("id"));
//        request.setAttribute("CurrentUser", user);
//    }

}
