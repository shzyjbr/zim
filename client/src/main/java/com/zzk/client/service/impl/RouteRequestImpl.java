package com.zzk.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zzk.client.thread.ContextHolder;
import com.zzk.client.vo.req.UserVO;
import com.zzk.client.vo.res.UserResVO;
import com.zzk.common.exception.ZIMException;
import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.routeapi.RouteApi;
import com.zzk.routeapi.vo.req.ChatReqVO;
import com.zzk.client.config.AppConfiguration;
import com.zzk.client.service.EchoService;
import com.zzk.client.service.RouteRequest;
import com.zzk.client.vo.req.GroupReqVO;
import com.zzk.client.vo.req.P2PReqVO;
import com.zzk.client.vo.res.ZIMServerResVO;
import com.zzk.client.vo.res.OnlineUsersResVO;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.proxy.ProxyManager;
import com.zzk.common.res.BaseResponse;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.res.LoginResVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 与网关交互， 这里用的是OKHttp来发送网络请求而不是通过netty
 *
 */
@Service
public class RouteRequestImpl implements RouteRequest {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteRequestImpl.class);

    @Autowired
    private OkHttpClient okHttpClient;

    @Value("${zim.route.url}")
    private String routeUrl ;

    @Autowired
    private EchoService echoService ;


    @Autowired
    private AppConfiguration appConfiguration ;

    @Autowired
    private ClientInfo clientInfo;

    @Override
    public void sendGroupMsg(GroupReqVO groupReqVO) throws Exception {
        //必须携带Token发送
//        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        Map<String, String> heads = new HashMap<>();
        heads.put("username", clientInfo.get().getUserName());
        heads.put("token", clientInfo.get().getToken());
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient, heads).getInstance();
        ChatReqVO chatReqVO = new ChatReqVO(groupReqVO.getUsername(), groupReqVO.getMsg()) ;
        Response response = null;
        try {
            response = (Response)routeApi.groupRoute(chatReqVO);
        }catch (Exception e){
            LOGGER.error("exception",e);
        }finally {
            response.body().close();
        }
    }

    /**
     * 客户端-> route服务器  通过http方式将消息发送给route服务器， route服务器再转发给 server，server再转发给其连接的client
     *
     * sourceClient ----http----> route服务器 ---http---> {server in serverList} ---netty----> {client in clientListOnServer}
     * @param p2PReqVO
     * @throws Exception
     */
    @Override
    public void sendP2PMsg(P2PReqVO p2PReqVO) throws Exception {
//        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        Map<String, String> heads = new HashMap<>();
        heads.put("username", clientInfo.get().getUserName());
        heads.put("token", clientInfo.get().getToken());
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient, heads).getInstance();
        com.zzk.routeapi.vo.req.P2PReqVO vo = new com.zzk.routeapi.vo.req.P2PReqVO() ;
        vo.setMsg(p2PReqVO.getMsg());
        vo.setReceiveUsername(p2PReqVO.getReceiveUserName());
        vo.setUsername(p2PReqVO.getUserName());

        Response response = null;
        try {
            response = (Response) routeApi.p2pRoute(vo);
            String json = response.body().string() ;
            BaseResponse baseResponse = JSON.parseObject(json, BaseResponse.class);

            // account offline.
            if (baseResponse.getCode().equals(StatusEnum.OFF_LINE.getCode())){
                LOGGER.error(p2PReqVO.getReceiveUserName() + ":" + StatusEnum.OFF_LINE.getMessage());
            }

        }catch (Exception e){
            LOGGER.error("exception",e);
        }finally {
            response.body().close();
        }
    }

    @Override
    public LoginResVO getZIMServer(LoginReqVO loginReqVO) throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        Response response = null;
        BaseResponse<LoginResVO>  res = null;
        try {
            response = (Response) routeApi.login(loginReqVO);
            String json = response.body().string();
            res = JSON.parseObject(json, new TypeReference<BaseResponse<LoginResVO>>(){});
            LOGGER.info("getCIMServer:{}", res);
            //重复失败  这里的逻辑很怪
            if (!res.getCode().equals(StatusEnum.SUCCESS.getCode())){
                echoService.echo(res.getMessage());
                // when client in reConnect state, could not exit.
                if (ContextHolder.getReconnect()){
                    echoService.echo("###{}###", StatusEnum.RECONNECT_FAIL.getMessage());
                    throw new ZIMException(StatusEnum.RECONNECT_FAIL);
                }
                System.exit(-1);
            }

        }catch (Exception e){
            LOGGER.error("exception",e);
        }finally {
            assert response != null;
            response.body().close();
        }

        return res.getDataBody();
    }

    @Override
    public Set<ZIMUserInfo> onlineUsers() throws Exception{
//        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        Map<String, String> heads = new HashMap<>();
        heads.put("username", clientInfo.get().getUserName());
        heads.put("token", clientInfo.get().getToken());
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient, heads).getInstance();
        Response response = null;
        BaseResponse<Set<ZIMUserInfo>> onlineUsersResVO = null;
        try {
            response = (Response) routeApi.onlineUser();
            String json = response.body().string() ;
            onlineUsersResVO = JSON.parseObject(json, new TypeReference<BaseResponse<Set<ZIMUserInfo>>>(){});

        }catch (Exception e){
            LOGGER.error("exception",e);
        }finally {
            if (response != null) {
                response.body().close();
            }
        }

        return onlineUsersResVO.getDataBody();
    }

    /**
     * 通过代理发送网络请求: client --offline--->  routeApi  客户端通过http方式向routeApi发送离线消息
     */
    @Override
    public void offLine() {
//        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        Map<String, String> heads = new HashMap<>();
        heads.put("username", clientInfo.get().getUserName());
        heads.put("token", clientInfo.get().getToken());
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient, heads).getInstance();
        ChatReqVO vo = new ChatReqVO(appConfiguration.getUserName(), "offLine") ;
        Response response = null;
        try {
            response = (Response) routeApi.offLine(vo);
        } catch (Exception e) {
            LOGGER.error("exception",e);
        } finally {
            response.body().close();
        }
    }
}
