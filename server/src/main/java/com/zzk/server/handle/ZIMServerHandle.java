package com.zzk.server.handle;


import com.zzk.common.constant.Constants;
import com.zzk.common.exception.ZIMException;
import com.zzk.common.kit.HeartBeatHandler;
import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.common.protocol.ZIMRequestProto;
import com.zzk.common.util.NettyAttrUtil;
import com.zzk.common.util.StringUtil;
import com.zzk.server.kit.RouteHandler;
import com.zzk.server.kit.ServerHeartBeatHandlerImpl;
import com.zzk.server.util.SessionSocketHolder;
import com.zzk.server.util.SpringBeanFactory;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ChannelHandler.Sharable
public class ZIMServerHandle extends SimpleChannelInboundHandler<ZIMRequestProto.ZIMReqProtocol> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZIMServerHandle.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channelActive!");
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channelInactive!!!");
        //可能出现业务判断离线后再次触发 channelInactive
        //通过userId拿出对应的Channel连接
        String username = SessionSocketHolder.getUsername((NioSocketChannel) ctx.channel()).split(";;")[0];
        if (username != null) {
            LOGGER.warn("[{}] trigger channelInactive offline!", username);

            //清除在线状态
            RouteHandler routeHandler = SpringBeanFactory.getBean(RouteHandler.class);
            routeHandler.userOffLine(username, (NioSocketChannel) ctx.channel());
            ctx.channel().close();
        }
    }


    /**
     * 客户端连接状态监测
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {

                LOGGER.info("定时检测客户端是否存活");

                HeartBeatHandler heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ZIMRequestProto.ZIMReqProtocol msg) throws Exception {
        LOGGER.info("channelRead0: received msg=[{}]", msg.toString());

        //验证token, 这样避免登录失效的客户端继续连接,而且也避免了其他人随意连接IM-Server
        String token = msg.getReqMsg();
        String username = msg.getUsername();
        RedisTemplate<String, String> redisTemplate = SpringBeanFactory.getBean("redisBean",RedisTemplate.class);
        String tokenInRedis = redisTemplate.opsForValue().get("zim-token:"+username);
        if (!StringUtils.equals(tokenInRedis, token)) {
            ctx.channel().close().sync();
            LOGGER.info("token 不一致");
        }

        if (msg.getType() == Constants.CommandType.LOGIN) {
            //保存客户端与 Channel 之间的关系: ['username;;token' -> NioSocketChannel]
            LOGGER.info("user:[{}], token:[{}]", msg.getUsername(), msg.getReqMsg());
            SessionSocketHolder.put(msg.getUsername(), (NioSocketChannel) ctx.channel());
            SessionSocketHolder.putUnique(msg.getUsername()+";;"+msg.getReqMsg(), (NioSocketChannel) ctx.channel());
            LOGGER.info("client [{}] online success!!", msg.getUsername());
        } else if (msg.getType() == Constants.CommandType.PING) { //心跳更新时间
            NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
            //向客户端响应 pong 消息
            ZIMRequestProto.ZIMReqProtocol heartBeat = SpringBeanFactory.getBean("heartBeat",
                    ZIMRequestProto.ZIMReqProtocol.class);
            ctx.writeAndFlush(heartBeat).addListeners((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    LOGGER.error("IO error,close Channel");
                    future.channel().close();
                }
            });
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (ZIMException.isResetByPeer(cause.getMessage())) {
            return;
        }

        LOGGER.error(cause.getMessage(), cause);

    }

}
