package com.zzk.server.kit;

import com.zzk.common.kit.HeartBeatHandler;
import com.zzk.common.util.NettyAttrUtil;
import com.zzk.server.config.AppConfiguration;
import com.zzk.server.util.SessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServerHeartBeatHandlerImpl implements HeartBeatHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerHeartBeatHandlerImpl.class);

    @Autowired
    private RouteHandler routeHandler ;

    @Autowired
    private AppConfiguration appConfiguration ;


    /**
     * 目前处理的是服务端检测客户端是否断连，但也可能服务器会挂掉，客户端要能够感知到服务端挂掉
     * @param ctx
     * @throws Exception
     */
    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {

        long heartBeatTime = appConfiguration.getHeartBeatTime() * 1000;

        Long lastReadTime = NettyAttrUtil.getReaderTime(ctx.channel());
        long now = System.currentTimeMillis();
        if (lastReadTime != null && now - lastReadTime > heartBeatTime){
            String username = SessionSocketHolder.getUsername((NioSocketChannel) ctx.channel());
            if (username != null){
                LOGGER.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!",username,now - lastReadTime);
            }
            routeHandler.userOffLine(username, (NioSocketChannel) ctx.channel());
            ctx.channel().close();
        }
    }
}
