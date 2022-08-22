package com.zzk.routeapi;


import com.zzk.routeapi.vo.req.ChatReqVO;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.req.P2PReqVO;
import com.zzk.routeapi.vo.req.RegisterInfoReqVO;
import com.zzk.routeapi.vo.res.RegisterInfoResVO;
import com.zzk.common.res.BaseResponse;


/**
 * 发送网络请求这一块可以重构一下，但是优先级不高
 */
public interface RouteApi {

    /**
     * group chat
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object groupRoute(ChatReqVO groupReqVO) throws Exception;

    /**
     * Point to point chat
     * @param p2pRequest
     * @return
     * @throws Exception
     */
    Object p2pRoute(P2PReqVO p2pRequest) throws Exception;


    /**
     * Offline account
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object offLine(ChatReqVO groupReqVO) throws Exception;

    /**
     * Login account
     * @param loginReqVO
     * @return
     * @throws Exception
     */
    Object login(LoginReqVO loginReqVO) throws Exception;

    /**
     * Register account
     *
     * @param registerInfoReqVO
     * @return
     * @throws Exception
     */
    BaseResponse<Object> registerAccount(RegisterInfoReqVO registerInfoReqVO) throws Exception;

    /**
     * Get all online users
     *
     * @return
     * @throws Exception
     */
    Object onlineUser() throws Exception;
}
