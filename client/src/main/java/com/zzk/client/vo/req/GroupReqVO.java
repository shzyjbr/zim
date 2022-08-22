package com.zzk.client.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 群发请求
 * 普通消息都是群聊消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupReqVO extends BaseRequest {

    @NotNull(message = "username 不能为空")
    private String username;


    @NotNull(message = "msg 不能为空")
    private String msg;

}
