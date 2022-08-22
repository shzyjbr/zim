package com.zzk.server.kit;

import com.zzk.routeapi.RouteApi;
import com.zzk.routeapi.vo.req.ChatReqVO;
import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.common.proxy.ProxyManager;
import com.zzk.server.config.AppConfiguration;
import com.zzk.server.util.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RouteHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(RouteHandler.class);

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private AppConfiguration configuration;

    /**
     * 用户下线
     *
     * @param username
     * @param channel
     * @throws IOException
     */
    public void userOffLine(String username, NioSocketChannel channel) throws IOException {
        if (username != null) {
            LOGGER.info("Account [{}] offline", username);
            //由IM-Server向网关发送用户下线事件
            //清除路由关系
            clearRouteInfo(username);
        }
        //清除对应的通道
        SessionSocketHolder.remove(channel);

    }


    /**
     * 清除路由关系
     * 向网关发送用户下线事件
     * @param username
     * @throws IOException
     */
    public void clearRouteInfo(String username) {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, configuration.getRouteUrl(), okHttpClient).getInstance();
        Response response = null;
        ChatReqVO vo = new ChatReqVO(username, "clear route relationship");
        try {
            response = (Response) routeApi.offLine(vo);
        } catch (Exception e){
            LOGGER.error("Exception",e);
        }finally {
            response.body().close();
        }
    }

}
