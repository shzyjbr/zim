package com.zzk.forwardroute.service;

import com.zzk.common.algorithm.RouteHandle;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.exception.ZIMException;
import com.zzk.common.pojo.RouteInfo;
import com.zzk.common.util.RouteInfoParseUtil;
import com.zzk.forwardroute.cache.ServerCache;
import com.zzk.forwardroute.kit.NetAddressIsReachable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-04-12 21:40
 * @since JDK 1.8
 */
@Component
public class CommonBizService {
    private static Logger logger = LoggerFactory.getLogger(CommonBizService.class) ;


    @Autowired
    private ServerCache serverCache ;

    @Autowired
    private RouteHandle routeHandle;

    /**
     * check ip and port
     * @param routeInfo
     */
    public boolean checkServerAvailable(RouteInfo routeInfo){
        //检测服务器是否可用，使用的方法是进行一次网络连接验证
        boolean reachable = NetAddressIsReachable.checkAddressReachable(routeInfo.getIp(), routeInfo.getCimServerPort(), 1000);
        if (!reachable) {
            logger.error("ip={}, port={} are not available", routeInfo.getIp(), routeInfo.getCimServerPort());
            //todo 这里用bug 检测连接不可用之后应该移除该IM服务器
            // rebuild cache 一旦有不可达IM-Server后就重新更新服务节点缓存


//            throw new ZIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        return reachable;

    }

    public String fetchServer(String username) {

        boolean reachable;
        String server;
        int i = 0;
        do {
            List<String> serverList = serverCache.getServerList();
            server = routeHandle.routeServer(serverList, username);
            RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
            reachable = checkServerAvailable(routeInfo);
            if (!reachable)
                serverCache.removeCache(server);
            i++;
        } while (!reachable && i < 3);
        if (!reachable) {
            throw new ZIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        return server;
    }
}
