package com.zzk.common.util;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * jwt工具类
 */
public class JWTUtils {
    /**
     * 实例
     */
    private static JWTUtils instance;

    /**
     * 发行者
     */
    private String subObject = "zzk";

    /**
     * 过期时间，默认30分钟
     */
    private long expired = 1000 * 60 * 30;

    /**
     * jwt构造
     */
    private static JwtBuilder jwtBuilder;

    /**
     * 密钥
     */
    private String secret = "0754zzk";// 密钥

    /**
     * 获取实例
     *
     * @return
     */
    public static JWTUtils getInstance() {
        if (instance == null) {
            instance = new JWTUtils();
        }
        jwtBuilder = Jwts.builder();
        return instance;
    }

    /**
     * 荷载信息(通常是一个User信息，还包括一些其他的元数据)
     *
     * @param key
     * @param val
     * @return
     */
    public JWTUtils setClaim(String key, Object val) {
        jwtBuilder.claim(key, val);
        return this;
    }

    /**
     * 生成 jwt token
     *
     * @return
     */
    public String generateToken() {
        String token = jwtBuilder
                .setSubject(subObject) // 发行者
                .setIssuedAt(new Date()) // 发行时间
                .setExpiration(new Date(System.currentTimeMillis() + expired))
                .signWith(SignatureAlgorithm.HS256, secret) // 签名类型 与 密钥
                .compressWith(CompressionCodecs.DEFLATE)// 对载荷进行压缩
                .compact(); // 压缩一下
        return token;
    }

    /**
     * 解析 token
     *
     * @param token
     * @return
     */
    public Claims check(String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
        }
        return null;
    }


    public String getSubObject() {
        return subObject;
    }

    /**
     * 设置发行者
     *
     * @param subObject
     * @return
     */
    public JWTUtils setSubObject(String subObject) {
        this.subObject = subObject;
        return this;
    }

    public long getExpired() {
        return expired;
    }

    /**
     * 设置过期时间
     *
     * @param expired
     * @return
     */
    public JWTUtils setExpired(long expired) {
        this.expired = expired;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    /**
     * 设置密钥
     *
     * @param secret
     * @return
     */
    public JWTUtils setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public static void main(String[] args) {
        JWTUtils jwt = JWTUtils.getInstance();
        String token = jwt
                .setClaim("uid", "123456")
                .setClaim("username", "zzc")
                .setExpired(50000)
                .generateToken();
        System.out.println(token);

        System.out.println("-----------------------");

        Claims claims = jwt.check(token);
        if (claims != null) {
            String id = (String) claims.get("uid");
            String name = (String) claims.get("username");
            Date issuedAt = claims.getIssuedAt();
            System.out.println(id);
            System.out.println(name);
            System.out.println(issuedAt);
        } else {
            System.out.println("非法token");
        }
    }
}