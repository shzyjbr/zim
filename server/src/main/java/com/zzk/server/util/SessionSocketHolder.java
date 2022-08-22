package com.zzk.server.util;

import com.zzk.common.pojo.ZIMUserInfo;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来存储与客户端的Channel连接及其对应的username的映射
 */
public class SessionSocketHolder {
    private static final Map<String, NioSocketChannel> CHANNEL_MAP = new ConcurrentHashMap<>(64);

    private static final Map<String, String> TOKEN_MAP = new ConcurrentHashMap<>(64);

    private static final Map<String, NioSocketChannel> UNIQUE_CHANNEL_MAP = new ConcurrentHashMap<>(64);


    public static void putUnique(String key, NioSocketChannel socketChannel) {
        UNIQUE_CHANNEL_MAP.put(key, socketChannel);
    }

    public static NioSocketChannel getUnique(String key) {
        return UNIQUE_CHANNEL_MAP.get(key);
    }

    public static void removeUnique(String key) {
        UNIQUE_CHANNEL_MAP.remove(key);
    }

    /**
     * Save the relationship between the username and the channel.
     * @param key
     * @param socketChannel
     */
    public static void put(String key, NioSocketChannel socketChannel) {
        CHANNEL_MAP.put(key, socketChannel);
    }

    public static NioSocketChannel get(String key) {
        return CHANNEL_MAP.get(key);
    }

    public static Map<String, NioSocketChannel> getRelationShip() {
        return CHANNEL_MAP;
    }

    public static void remove(NioSocketChannel nioSocketChannel) {
        final String[] username = new String[1];
        CHANNEL_MAP.entrySet().stream().filter(entry -> entry.getValue() == nioSocketChannel).forEach(entry -> {
            CHANNEL_MAP.remove(entry.getKey());
            username[0] = entry.getKey();
        });
        String token = TOKEN_MAP.get(username[0]);
        UNIQUE_CHANNEL_MAP.remove(username[0] + ";;"+token);
    }

    public static String getToken(String username) {
        return TOKEN_MAP.get(username);
    }
    public static void saveToken(String username, String token) {
        TOKEN_MAP.put(username, token);
    }

    /**
     * 获取注册用户信息
     * @param nioSocketChannel
     * @return
     */
    public static String getUsername(NioSocketChannel nioSocketChannel){
        for (Map.Entry<String, NioSocketChannel> entry : CHANNEL_MAP.entrySet()) {
            NioSocketChannel value = entry.getValue();
            if (nioSocketChannel == value){
                String username = entry.getKey();
                return username ;
            }
        }
        return null;
    }



}
