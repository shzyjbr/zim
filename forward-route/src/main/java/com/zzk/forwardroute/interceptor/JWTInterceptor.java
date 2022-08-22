package com.zzk.forwardroute.interceptor;

import com.alibaba.fastjson.JSON;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.res.BaseResponse;
import com.zzk.common.util.JWTUtils;
import com.zzk.forwardroute.kit.UIDGenerator;
import com.zzk.routeapi.vo.res.ZIMServerResVO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static com.zzk.forwardroute.constant.Constant.ACCOUNT_PREFIX;
import static com.zzk.forwardroute.constant.Constant.TOKEN_PREFIX;

@Component
@CrossOrigin
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if ("OPTIONS".equals(method)){
            return true;
        }
        //获取post请求体比较麻烦，为简单起见，这里client将uid设置到header中
        String username = request.getHeader("username");
        log.info("JWTInterceptor:username:{}",username );
        //获取请求头部信息
        String token = request.getHeader("token");
        log.info("JWTInterceptor:token:{}",token );
        if (token!=null) {
            try {
                JwtParser jwtParser = Jwts.parser();
                jwtParser.setSigningKey("0754zzk");
                //只做token验证，若不通过，则抛异常
                Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

                Claims claims = claimsJws.getBody();
                if (claims == null) {
                    doResponse(response,"Token不合法！");
                    return false;
                }
                String usernameInToken = claims.get("username", String.class);
                if (!StringUtils.equals(usernameInToken, username))
                    doResponse(response,"Token不合法！");
                //  token合法之后还要比对redis中的token是否一致
                String key = TOKEN_PREFIX + username;
                String tokenInRedis = redisTemplate.opsForValue().get(key);
                if (!StringUtils.equals(tokenInRedis, token)) {
                    doResponse(response,"Token已过期，请重新登陆！");
                    return false;
                }
                //token续期
                redisTemplate.expire(key,30*60, TimeUnit.SECONDS);
                return true;
            }catch (ExpiredJwtException e){
                doResponse(response,"Token已过期，请重新登陆！");
                return false;
            } catch (Exception e){
                doResponse(response,"Token不合法！");
                return false;
            }
        } else {
            //没有携带token
            doResponse(response,"Token不合法！");
            return false;
        }
    }

    public void doResponse(HttpServletResponse response,String info) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        BaseResponse res = new BaseResponse<>();
        res.setCode(StatusEnum.FAIL.getCode());
        res.setMessage(info);
        PrintWriter writer = response.getWriter();
        String json = JSON.toJSONString(res);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
