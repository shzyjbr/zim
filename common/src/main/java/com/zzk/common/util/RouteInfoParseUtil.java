package com.zzk.common.util;

import com.zzk.common.exception.ZIMException;
import com.zzk.common.pojo.RouteInfo;

import static com.zzk.common.enums.StatusEnum.VALIDATION_FAIL;

public class RouteInfoParseUtil {

    public static RouteInfo parse(String info){
        try {
            String[] serverInfo = info.split(":");
            RouteInfo routeInfo =  new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]),Integer.parseInt(serverInfo[2])) ;
            return routeInfo ;
        }catch (Exception e){
            throw new ZIMException(VALIDATION_FAIL) ;
        }
    }
}
