package com.zzk.routeapi.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2PReqVO extends BaseRequest {

    /**
     * 消息发送者的 userId   如zzk
     */
    private String username ;


    /**
     * 消息接收者的 username  比如 zhangsan
     */
    private String receiveUsername ;
    /**
     * 消息内容
     */
    private String msg ;

}
