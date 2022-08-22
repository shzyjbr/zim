package com.zzk.server.controller;

import com.zzk.common.constant.Constants;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.protocol.ZIMRequestProto;
import com.zzk.common.res.BaseResponse;
import com.zzk.server.server.ZIMServer;
import com.zzk.server.util.SessionSocketHolder;
import com.zzk.serverapi.ServerApi;
import com.zzk.serverapi.vo.req.SendMsgReqVO;
import com.zzk.serverapi.vo.res.SendMsgResVO;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 提供给网关使用的转发消息接口
 */
@Slf4j
@Controller
@RequestMapping("/")
public class IndexController implements ServerApi {

    @Autowired
    private ZIMServer cimServer;


    /**
     *
     * @param sendMsgReqVO
     * @return
     */
    @Override
    @RequestMapping(value = "sendMsg",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<SendMsgResVO> sendMsg(@RequestBody SendMsgReqVO sendMsgReqVO){
        BaseResponse<SendMsgResVO> res = new BaseResponse<>();
        cimServer.sendMsg(sendMsgReqVO) ;

        SendMsgResVO sendMsgResVO = new SendMsgResVO() ;
        sendMsgResVO.setMsg("OK") ;
        res.setCode(StatusEnum.SUCCESS.getCode()) ;
        res.setMessage(StatusEnum.SUCCESS.getMessage()) ;
        res.setDataBody(sendMsgResVO) ;
        return res ;
    }

    /**
     *
     * @param sendMsgReqVO
     * @return
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "offlineRepeatUser",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> offlineRepeatUser(@RequestBody SendMsgReqVO sendMsgReqVO) {
        BaseResponse<String> res = new BaseResponse<>();
        String key = sendMsgReqVO.getUsername() + ";;" + sendMsgReqVO.getMsg();
        NioSocketChannel nioSocketChannel = SessionSocketHolder.getUnique(key);
        if (null == nioSocketChannel) {
            log.error("client {} already offline!", sendMsgReqVO.getUsername()+";;"+sendMsgReqVO.getMsg());
            res.setCode(StatusEnum.FAIL.getCode()) ;
            res.setMessage(StatusEnum.FAIL.getMessage()) ;
            res.setDataBody("下线重复登录用户失败！！！") ;
            return res;
        }
        //复用类型
        ZIMRequestProto.ZIMReqProtocol protocol = ZIMRequestProto.ZIMReqProtocol.newBuilder()
                .setUsername(sendMsgReqVO.getUsername())
                .setReqMsg("repeatLogin")
                .setType(Constants.CommandType.PING)
                .build();

        ChannelFuture future = nioSocketChannel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                log.info("server push offline msg:[{}]", sendMsgReqVO.toString()));
        SessionSocketHolder.removeUnique(key);
        res.setCode(StatusEnum.SUCCESS.getCode()) ;
        res.setMessage(StatusEnum.SUCCESS.getMessage()) ;
        res.setDataBody("成功下线重复登录用户") ;
        return res;
    }


}
