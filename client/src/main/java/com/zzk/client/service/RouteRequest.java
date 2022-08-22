package com.zzk.client.service;

import com.zzk.client.vo.req.GroupReqVO;
import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.client.vo.req.P2PReqVO;
import com.zzk.client.vo.res.OnlineUsersResVO;
import com.zzk.routeapi.vo.res.LoginResVO;

import java.util.List;
import java.util.Set;


public interface RouteRequest {

    /**
     * 群发消息
     * @param groupReqVO 消息
     * @throws Exception
     */
    void sendGroupMsg(GroupReqVO groupReqVO) throws Exception;


    /**
     * 私聊
     * @param p2PReqVO
     * @throws Exception
     */
    void sendP2PMsg(P2PReqVO p2PReqVO)throws Exception;

    /**
     * 客户端先通过网关拿到一台IM-Server
     * 获取服务器
     * @return 服务ip+port
     * @param loginReqVO
     * @throws Exception
     */
    LoginResVO getZIMServer(LoginReqVO loginReqVO) throws Exception;


    /**
     * 获取所有在线用户
     * @return
     * @throws Exception
     */
    Set<ZIMUserInfo> onlineUsers()throws Exception ;


    void offLine() ;

}
