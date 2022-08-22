package com.zzk.client.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Google Protocol 编解码发送
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleProtocolVO extends BaseRequest {
    @NotNull(message = "username不能为空")
    private String username ;

    @NotNull(message = "msg 不能为空")
    private String msg ;


}
