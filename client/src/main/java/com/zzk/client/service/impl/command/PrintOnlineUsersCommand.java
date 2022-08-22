package com.zzk.client.service.impl.command;

import com.zzk.client.service.EchoService;
import com.zzk.client.service.InnerCommand;
import com.zzk.client.service.RouteRequest;
import com.zzk.client.vo.res.OnlineUsersResVO;
import com.zzk.common.pojo.ZIMUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class PrintOnlineUsersCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(PrintOnlineUsersCommand.class);


    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        try {
            Set<ZIMUserInfo> onlineUsers = routeRequest.onlineUsers();

            echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            for (ZIMUserInfo onlineUser : onlineUsers) {
                echoService.echo("=====userName=[{}],uid=[{}]=====", onlineUser.getUserName(), onlineUser.getUid());
            }
            echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
    }
}
