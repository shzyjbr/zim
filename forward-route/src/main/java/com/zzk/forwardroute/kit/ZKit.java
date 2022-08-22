package com.zzk.forwardroute.kit;

import com.alibaba.fastjson.JSON;
import com.zzk.forwardroute.cache.ServerCache;
import com.zzk.forwardroute.constant.Constant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ZKit {

    private static Logger logger = LoggerFactory.getLogger(ZKit.class);


    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ServerCache serverCache ;



    /**
     * 监听事件
     *
     * @param path
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path, (parentPath, currentChildren) -> {
            logger.info("Clear and update local cache parentPath=[{}],currentChildren=[{}]", parentPath,currentChildren.toString());

            //update local cache, delete and save.
            serverCache.updateCache(currentChildren) ;
        });


    }


    /**
     * get all server node from zookeeper
     * @return
     */
    public List<String> getAllNode(){
        //ip:import:port
        List<String> children = zkClient.getChildren(Constant.ROUTE_ZK_ROOT_PATH);
        logger.info("Query all node =[{}] success.", JSON.toJSONString(children));
       return children;
    }


}
