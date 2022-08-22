package com.zzk.client.service.impl;


import com.zzk.client.client.ZIMClient;
import com.zzk.client.config.AppConfiguration;
import com.zzk.client.service.*;
import com.zzk.client.vo.req.GroupReqVO;
import com.zzk.client.vo.req.P2PReqVO;
import com.zzk.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class MsgHandler implements MsgHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(MsgHandler.class);
    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private AppConfiguration configuration;

    @Resource(name = "callBackThreadPool")
    private ThreadPoolExecutor executor;

    @Autowired
    private ZIMClient ZIMClient;

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private InnerCommandContext innerCommandContext ;

    private boolean aiModel = false;

    @Override
    public void sendMsg(String msg) {
        normalChat(msg);
    }

    /**
     * 正常聊天
     *
     * @param msg
     */
    private void normalChat(String msg) {
        String[] totalMsg = msg.split(";;");
        /**
         * 这个判断逻辑非常简易，有出错的可能
         * 比如别人消息中打了;;   那么就会进入私聊分支， 其实只是打错了
         * 这里是可以改进的点
         */
        // todo 改进这里的群聊私聊判断
        if (totalMsg.length > 1) {
            //私聊
            P2PReqVO p2PReqVO = new P2PReqVO();
            p2PReqVO.setUserName(configuration.getUserName());
            //todo 校验是否是一个合法的username或者该user在线
            p2PReqVO.setReceiveUserName(totalMsg[0]);
            p2PReqVO.setMsg(totalMsg[1]);
            try {
                p2pChat(p2PReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }

        } else {
            //群聊 用username标识
            GroupReqVO groupReqVO = new GroupReqVO(configuration.getUserName(), msg);
            try {
                groupChat(groupReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }
        }
    }


    @Override
    public void groupChat(GroupReqVO groupReqVO) throws Exception {
        routeRequest.sendGroupMsg(groupReqVO);
    }

    @Override
    public void p2pChat(P2PReqVO p2PReqVO) throws Exception {

        routeRequest.sendP2PMsg(p2PReqVO);

    }

    @Override
    public boolean checkMsg(String msg) {
        if (StringUtil.isEmpty(msg)) {
            LOGGER.warn("不能发送空消息！");
            return true;
        }
        return false;
    }

    @Override
    public boolean innerCommand(String msg) {

        if (msg.startsWith(":")) {

            InnerCommand instance = innerCommandContext.getInstance(msg);
            instance.process(msg) ;

            return true;

        } else {
            return false;
        }


    }

    /**
     * 关闭系统
     */
    @Override
    public void shutdown() {
        LOGGER.info("系统关闭中。。。。");
        //向网关发送客户端离线事件
        routeRequest.offLine();
        msgLogger.stop();
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("线程池关闭中。。。。");
            }
            ZIMClient.close();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        System.exit(0);
    }


}
