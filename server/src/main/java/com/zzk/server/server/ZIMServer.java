package com.zzk.server.server;

import com.zzk.common.constant.Constants;
import com.zzk.common.protocol.ZIMRequestProto;
import com.zzk.serverapi.vo.req.SendMsgReqVO;
import com.zzk.server.init.ZIMServerInitializer;
import com.zzk.server.util.SessionSocketHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * 1. 接收转发消息，转送给客户端
 * 2. 用户断联向网关发送用户下线事件
 * clientA -> 网关 -> IM-Server -> clientB
 */
@Component
public class ZIMServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZIMServer.class);

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();


    @Value("${zim.server.port}")
    private int nettyPort;


    /**
     * 启动 zim server
     *
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ZIMServerInitializer());

        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            LOGGER.info("Start zim server success!!!");
        }
    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        LOGGER.info("Close zim server success!!!");
    }


    /**
     * Push msg to client.
     * @param sendMsgReqVO 消息
     */
    public void sendMsg(SendMsgReqVO sendMsgReqVO){
        NioSocketChannel socketChannel = SessionSocketHolder.get(sendMsgReqVO.getUsername());
        LOGGER.info("SendMsgReqVO:{}",sendMsgReqVO);
        if (null == socketChannel) {
            LOGGER.error("client {} offline!", sendMsgReqVO.getUsername());
            return;
        }
        ZIMRequestProto.ZIMReqProtocol protocol = ZIMRequestProto.ZIMReqProtocol.newBuilder()
                .setUsername(sendMsgReqVO.getUsername())
                .setReqMsg(sendMsgReqVO.getMsg())
                .setType(Constants.CommandType.MSG)
                .build();

        ChannelFuture future = socketChannel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("server push msg:[{}]", sendMsgReqVO.toString()));
    }
}
