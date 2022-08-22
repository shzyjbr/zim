package com.zzk.forwardroute.service;


import com.zzk.routeapi.vo.req.ChatReqVO;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.res.ZIMServerResVO;
import com.zzk.routeapi.vo.res.RegisterInfoResVO;
import com.zzk.common.enums.StatusEnum;

import java.util.Map;

/**
 *  账户服务

 */
public interface AccountService {

    /**
     * 注册用户
     * @param info 用户信息
     * @return
     * @throws Exception
     */
    RegisterInfoResVO register(RegisterInfoResVO info) throws Exception;

    /**
     * 登录服务
     * @param loginReqVO 登录信息
     * @return true 成功 false 失败
     * @throws Exception
     */
    StatusEnum login(LoginReqVO loginReqVO) throws Exception ;

    /**
     * 保存路由信息
     * @param msg 服务器信息
     * @param loginReqVO 用户信息
     * @throws Exception
     */
    void saveRouteInfo(LoginReqVO loginReqVO ,String msg) throws Exception ;

    /**
     * 加载所有用户的路由关系
     * @return 所有的路由关系
     */
    Map<String, ZIMServerResVO> loadRouteRelated() ;

    /**
     * 获取某个用户的路由关系
     * @param username
     * @return 获取某个用户的路有关系
     */
    ZIMServerResVO loadRouteRelatedByUsername(String username) ;


    /**
     * 用户下线
     * @param userId 下线用户ID
     * @throws Exception
     */
    void offLine(String username) throws Exception;

    /**
     * 推送消息
     * @param zimServerResVO
     * @param groupReqVO 消息
     * @param sendUsername 发送者的username
     * @throws Exception
     */
    void pushMsg(ZIMServerResVO zimServerResVO, String sendUsername , ChatReqVO groupReqVO) throws Exception;

    void pushOfflineMsg(ZIMServerResVO zimServerResVO, ChatReqVO groupReqVO) throws Exception;
}
