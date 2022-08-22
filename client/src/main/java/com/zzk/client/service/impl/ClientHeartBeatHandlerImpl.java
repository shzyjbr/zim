package com.zzk.client.service.impl;

import com.zzk.client.client.ZIMClient;
import com.zzk.client.thread.ContextHolder;
import com.zzk.common.kit.HeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClientHeartBeatHandlerImpl implements HeartBeatHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHeartBeatHandlerImpl.class);

    @Autowired
    private ZIMClient ZIMClient;


    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        /**
         * 在这个方法中没用上ChannelHandlerContext
         */
        //重连
        ContextHolder.setReconnect(true);
        ZIMClient.reconnect();

    }


}
