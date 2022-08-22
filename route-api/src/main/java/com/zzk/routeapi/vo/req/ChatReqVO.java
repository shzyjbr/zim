package com.zzk.routeapi.vo.req;

import com.zzk.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatReqVO extends BaseRequest {

    private String username ;


    private String msg ;



}
