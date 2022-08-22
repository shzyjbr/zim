package com.zzk.client.service.impl.command;

import com.zzk.client.service.EchoService;
import com.zzk.client.service.InnerCommand;
import com.zzk.client.service.RouteRequest;
import com.zzk.client.vo.res.OnlineUsersResVO;
import com.zzk.common.construct.TrieTree;
import com.zzk.common.pojo.ZIMUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PrefixSearchCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(PrefixSearchCommand.class);


    @Autowired
    private RouteRequest routeRequest ;
    @Autowired
    private EchoService echoService ;

    @Override
    public void process(String msg) {
        try {
            //向网关获取所有在线用户
            Set<ZIMUserInfo> onlineUsers = routeRequest.onlineUsers();
            //使用线段树进行前缀匹配
            TrieTree trieTree = new TrieTree();
            for (ZIMUserInfo onlineUser : onlineUsers) {
                trieTree.insert(onlineUser.getUserName());
            }

            String[] split = msg.split(" ");
            String key = split[1];
            List<String> list = trieTree.prefixSearch(key);

            //  '\031;4m' 后面的字符为红色且带下划线 ‘033[0m’后面的字符无样式
            for (String res : list) {
                res = res.replace(key, "\033[31;4m" + key + "\033[0m");
                echoService.echo(res) ;
            }

        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
    }
}
