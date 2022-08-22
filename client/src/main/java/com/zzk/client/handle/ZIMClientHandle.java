package com.zzk.client.handle;

import com.vdurmont.emoji.EmojiParser;
import com.zzk.client.service.EchoService;
import com.zzk.client.service.ReConnectManager;
import com.zzk.client.service.ShutDownMsg;
import com.zzk.client.service.impl.ClientInfo;
import com.zzk.client.service.impl.EchoServiceImpl;
import com.zzk.client.util.SpringBeanFactory;
import com.zzk.common.constant.Constants;
import com.zzk.common.protocol.ZIMRequestProto;
import com.zzk.common.protocol.ZIMResponseProto;
import com.zzk.common.util.NettyAttrUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@ChannelHandler.Sharable
public class ZIMClientHandle extends SimpleChannelInboundHandler<ZIMResponseProto.ZIMResProtocol> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZIMClientHandle.class);

    private MsgHandleCaller caller ;

    private ThreadPoolExecutor threadPoolExecutor ;

    private ScheduledExecutorService scheduledExecutorService ;

    private ReConnectManager reConnectManager ;

    private ShutDownMsg shutDownMsg ;

    private EchoService echoService ;

    private ClientInfo clientInfo;


    /**
     * 服务端是看读超时，就是看客户端多久没有发送消息了
     * 客户端一般是写超时，就是多久没有写数据了，此时就需要发送一个心跳包，告诉服务器端自己还在连接着
     * 这里是客户端
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt ;
            clientInfo = SpringBeanFactory.getBean(com.zzk.client.service.impl.ClientInfo.class);
            if (idleStateEvent.state() == IdleState.WRITER_IDLE){
//                ZIMRequestProto.ZIMReqProtocol heartBeat = SpringBeanFactory.getBean("heartBeat",
//                        ZIMRequestProto.ZIMReqProtocol.class);
                ZIMRequestProto.ZIMReqProtocol heart = ZIMRequestProto.ZIMReqProtocol.newBuilder()
                        .setUsername(clientInfo.get().getUserName())
                        .setReqMsg(clientInfo.get().getToken())
                        .setType(Constants.CommandType.PING)
                        .build();
                ctx.writeAndFlush(heart).addListeners((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        LOGGER.error("IO error,close Channel");
                        future.channel().close();
                    }
                }) ;
            }

        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端和服务端建立连接时调用
        LOGGER.info("cim server connect success!");
    }

    /**
     * 在这里处理重连逻辑
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        if (shutDownMsg == null){
            shutDownMsg = SpringBeanFactory.getBean(ShutDownMsg.class) ;
        }

        //用户主动退出，不执行重连逻辑
        if (shutDownMsg.checkStatus()){
            return;
        }

        if (scheduledExecutorService == null){
            scheduledExecutorService = SpringBeanFactory.getBean("scheduledTask",ScheduledExecutorService.class) ;
            reConnectManager = SpringBeanFactory.getBean(ReConnectManager.class) ;
        }
        LOGGER.info("客户端断开了，重新连接！");
        reConnectManager.reConnect(ctx);
    }


    /**
     * 接受读消息事件处理， 当前处理两类消息， 一个是服务器发送过来的心跳消息， 一个是聊天消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ZIMResponseProto.ZIMResProtocol msg) throws Exception {
        if (echoService == null){
            echoService = SpringBeanFactory.getBean(EchoServiceImpl.class) ;
        }


        //心跳更新时间
        if (msg.getType() == Constants.CommandType.PING){
            //合理应该是新开一个repeat login类型，但是这里为了快速开发就复用了PING状态，根据msg内容来区分
            if ("repeatLogin".equals(msg.getResMsg())) {
                echoService.echo("您被挤占下线！！！！");
                ctx.channel().close();
                System.exit(0);
            } else {
                //LOGGER.info("收到服务端心跳！！！");
                NettyAttrUtil.updateReaderTime(ctx.channel(),System.currentTimeMillis());
            }


        }

        if (msg.getType() != Constants.CommandType.PING) {
            //回调消息, 目前主要是把消息持久化
            callBackMsg(msg.getResMsg());

            //将消息中的 emoji 表情格式化为 Unicode 编码以便在终端可以显示
            String response = EmojiParser.parseToUnicode(msg.getResMsg());
            echoService.echo(response);
        }
    }

    /**
     * 回调消息
     * 目前做的是把聊天消息持久化
     * @param msg
     */
    private void callBackMsg(String msg) {
        threadPoolExecutor = SpringBeanFactory.getBean("callBackThreadPool",ThreadPoolExecutor.class) ;
        threadPoolExecutor.execute(() -> {
            //这里的MsgHandleCaller虽然没有@Bean修饰，但是在BeanConfig中配置了，由Spring容器管理
            caller = SpringBeanFactory.getBean(MsgHandleCaller.class) ;
            caller.getMsgHandleListener().handle(msg);
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        cause.printStackTrace() ;
        ctx.close() ;
    }
}
