package com.zzk.serverapi.vo.req;


import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg ;

    @NotNull(message = "username 不能为空")
    private String username ;


}
