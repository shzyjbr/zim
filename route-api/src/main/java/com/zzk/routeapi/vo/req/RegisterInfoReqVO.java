package com.zzk.routeapi.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfoReqVO extends BaseRequest {

    private String userName ;

    private String password;

}
