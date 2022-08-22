package com.zzk.client.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqVO extends BaseRequest {
    private String userName;
    private String password;
}
