package com.zzk.client.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2PReqVO extends BaseRequest {

    @NotNull(message = "userName 不能为空")
    private String userName ;


    @NotNull(message = "receiveUserName 不能为空")
    private String receiveUserName ;


    @NotNull(message = "msg 不能为空")
    private String msg ;


}
