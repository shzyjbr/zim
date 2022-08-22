package com.zzk.routeapi.vo.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResVO {

    private String uid;
    private String username;
    private String token;
    private ZIMServerResVO routedServer;
}
