package com.zzk.client.service;


public interface CustomMsgHandleListener {

    /**
     * 消息回调
     * @param msg
     */
    void handle(String msg);
}
