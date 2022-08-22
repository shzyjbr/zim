package com.zzk.client.service.impl;

import com.zzk.client.service.CustomMsgHandleListener;
import com.zzk.client.service.MsgLogger;
import com.zzk.client.util.SpringBeanFactory;

/**
 * 收到消息回调, 收到消息后调用回调函数， 将消息持久化
 */
public class MsgCallBackListener implements CustomMsgHandleListener {


    private MsgLogger msgLogger ;

    public MsgCallBackListener() {
        this.msgLogger = SpringBeanFactory.getBean(MsgLogger.class) ;
    }

    @Override
    public void handle(String msg) {
        msgLogger.log(msg) ;
    }
}
