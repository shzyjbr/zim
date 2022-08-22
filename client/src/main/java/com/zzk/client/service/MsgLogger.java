package com.zzk.client.service;

/**
 * 客户端本地聊天记录持久化接口
 */
public interface MsgLogger {

    /**
     * 异步写入消息
     * @param msg
     */
    void log(String msg) ;


    /**
     * 停止写入
     */
    void stop() ;

    /**
     * 查询聊天记录
     * @param key 关键字
     * @return
     */
    String query(String key) ;
}
