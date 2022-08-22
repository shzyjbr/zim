package com.zzk.serverapi;

import com.zzk.serverapi.vo.req.SendMsgReqVO;


/**
 * 抽象出一个接口是为了用动态代理，方便进行代码封装
 */
public interface ServerApi {

    /**
     * Push msg to client
     * @param sendMsgReqVO
     * @return
     * @throws Exception
     */
    Object sendMsg(SendMsgReqVO sendMsgReqVO) throws Exception;

    Object offlineRepeatUser(SendMsgReqVO sendMsgReqVO) throws Exception;
}
