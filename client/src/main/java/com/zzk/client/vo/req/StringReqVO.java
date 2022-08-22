package com.zzk.client.vo.req;

import com.zzk.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

public class StringReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
