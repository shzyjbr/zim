package com.zzk.client.client;

import com.zzk.client.config.AppConfiguration;
import com.zzk.client.init.ZIMClientHandleInitializer;
import com.zzk.client.service.EchoService;
import com.zzk.client.service.MsgHandle;
import com.zzk.client.service.ReConnectManager;
import com.zzk.client.service.RouteRequest;
import com.zzk.client.service.impl.ClientInfo;
import com.zzk.client.thread.ContextHolder;
import com.zzk.client.vo.req.GoogleProtocolVO;
import com.zzk.common.constant.Constants;
import com.zzk.common.protocol.ZIMRequestProto;
import com.zzk.routeapi.vo.res.LoginResVO;
import com.zzk.routeapi.vo.res.ZIMServerResVO;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Slf4j
@Component
public class ZIMClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZIMClient.class);

    //nThreads 设置为0, Netty会自适应设置线程数量为max(1, 本机处理器*2)
    //super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, threadFactory, args);
    private EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("zim-work"));

    @Value("${zim.user.password}")
    private String password;

    @Value("${zim.user.userName}")
    private String userName;

    private SocketChannel channel;

    @Autowired
    private EchoService echoService;

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private AppConfiguration configuration;

    @Autowired
    private MsgHandle msgHandle;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private ReConnectManager reConnectManager;

    /**
     * 重试次数
     */
    private int reconnectCount;


    /**
     * bean初始化后调用start()
     *
     * @throws Exception
     */
    @PostConstruct
    public void start() throws Exception {

        //登录 + 获取可以使用的服务器 ip+port
        ZIMServerResVO zimServer = userLogin();

        //启动客户端
        startClient(zimServer);

        //向服务端注册
        loginZIMServer();


    }

    /**
     * 启动客户端
     *
     * @param zimServer
     * @throws Exception
     */
    private void startClient(ZIMServerResVO zimServer) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ZIMClientHandleInitializer());

        ChannelFuture future = null;
        try {
            //连接到ZIM的一台服务器
            future = bootstrap.connect(zimServer.getIp(), zimServer.getZimServerPort()).sync();
        } catch (Exception e) {
            //todo 当前连接服务器挂掉后，重连新的服务器有bug
            reconnectCount++;

            if (reconnectCount >= configuration.getErrorCount()) {
                LOGGER.error("连接失败次数达到上限[{}]次", reconnectCount);
                msgHandle.shutdown();
            }
            LOGGER.error("Connect fail!", e);
        }

        if (future!= null && future.isSuccess()) {
            echoService.echo("成功连接到IM-Server");
            LOGGER.info("成功连接到IM-Server:[{}:{}]",zimServer.getIp(), zimServer.getZimServerPort());
        } else {
            LOGGER.error("启动 zim client 失败");
            System.exit(-1);
        }
        channel = (SocketChannel) future.channel();
    }

    /**
     * 登录并获取一台IM-Server
     *
     * @return 路由的IM-Server信息
     * @throws Exception
     */
    private ZIMServerResVO userLogin() throws InterruptedException {
        com.zzk.routeapi.vo.req.LoginReqVO loginReqVO = new com.zzk.routeapi.vo.req.LoginReqVO();
        loginReqVO.setUserName(userName);
        loginReqVO.setPassword(password);
        LoginResVO loginInfo = null;
        com.zzk.routeapi.vo.res.ZIMServerResVO serverResVO = null;
        try {
            loginInfo = routeRequest.getZIMServer(loginReqVO);
            log.info("ZIMServerResVO-loginInfo: {}", loginInfo);
            //保存客户端所连接的IM-Server、uid、userName等信息
            serverResVO = loginInfo.getRoutedServer();
            clientInfo.saveServiceInfo(serverResVO.getIp() + ":" + serverResVO.getZimServerPort())
                    .saveToken(loginInfo.getToken())
                    .saveUserInfo(loginInfo.getUid(), loginInfo.getUsername());
            LOGGER.info("zimServer=[{}]", serverResVO);
        } catch (Exception e) {
            reconnectCount++;
            //三秒重连一次
            Thread.sleep(3000);
            if (reconnectCount >= configuration.getErrorCount()) {
                echoService.echo("The maximum number of reconnections has been reached[{}]times, close zim client!", reconnectCount);
                msgHandle.shutdown();
            }
            LOGGER.error("login fail", e);
        }
        return serverResVO;
    }

    /**
     * 向服务器注册
     */
    private void loginZIMServer() {
        //注册到IM-Server 首次连接需要携带上token， 这里直接把token放进msg中
        ZIMRequestProto.ZIMReqProtocol login = ZIMRequestProto.ZIMReqProtocol.newBuilder()
                .setUsername(userName)
                .setReqMsg(clientInfo.get().getToken())
                .setType(Constants.CommandType.LOGIN)
                .build();
        ChannelFuture future = channel.writeAndFlush(login);
        future.addListener((ChannelFutureListener) channelFuture ->
                echoService.echo("Registry zim server success!")
        );
    }

    /**
     * 发送消息字符串
     *
     * @param msg
     */
    public void sendStringMsg(String msg) {
        ByteBuf message = Unpooled.buffer(msg.getBytes().length);
        message.writeBytes(msg.getBytes());
        ChannelFuture future = channel.writeAndFlush(message);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("客户端手动发消息成功={}", msg));

    }

    /**
     * 发送 Google Protocol 编解码字符串
     *
     * @param googleProtocolVO
     */
    public void sendGoogleProtocolMsg(GoogleProtocolVO googleProtocolVO) {

        ZIMRequestProto.ZIMReqProtocol protocol = ZIMRequestProto.ZIMReqProtocol.newBuilder()
                .setUsername(googleProtocolVO.getUsername())
                .setReqMsg(googleProtocolVO.getMsg())
                .setType(Constants.CommandType.MSG)
                .build();


        ChannelFuture future = channel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("客户端手动发送 Google Protocol 成功={}", googleProtocolVO.toString()));

    }


    /**
     * 1. clear route information.
     * 2. reconnect.
     * 3. shutdown reconnect job.
     * 4. reset reconnect state.
     *
     * @throws Exception
     */
    public void reconnect() throws Exception {
        if (channel != null && channel.isActive()) {
            return;
        }
        //首先清除路由信息，下线
        routeRequest.offLine();

//        echoService.echo("断开与服务器连接，5秒后开始重新连接....");
//        Thread.sleep(5000);
        start();
        echoService.echo("重新连接成功!!!");
        reConnectManager.reConnectSuccess();
        ContextHolder.clear();
    }

    /**
     * 关闭
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        if (channel != null) {
            channel.close();
        }
    }
}
