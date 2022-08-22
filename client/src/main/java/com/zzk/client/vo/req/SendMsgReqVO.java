package com.zzk.client.vo.req;


import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg ;

    @NotNull(message = "username 不能为空")
    private String username ;


}
