package com.zzk.routeapi.vo.req;

import com.zzk.common.req.BaseRequest;

public class SendMsgReqVO extends BaseRequest {

    private String msg ;

    private long id ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
