package com.zzk.forwardroute.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.LoadingCache;
import com.zzk.forwardroute.constant.Constant;
import com.zzk.forwardroute.kit.ZKit;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器节点缓存
 */
@Component
public class ServerCache {

    private static Logger logger = LoggerFactory.getLogger(ServerCache.class);


    @Autowired
    private LoadingCache<String, String> cache;

    @Autowired
    private ZkClient zkClient;

//
//    @Autowired
//    private ZKit zkUtil;

    public void addCache(String key) {
        cache.put(key, key);
    }

    public void removeCache(String key) {
        cache.invalidate(key);
    }

    /**
     * 更新所有缓存/先删除 再新增
     *
     * @param currentChildren
     */
    public void updateCache(List<String> currentChildren) {
        cache.invalidateAll();
        for (String currentChild : currentChildren) {
            // currentChildren=ip-127.0.0.1:11212:9082 or 127.0.0.1:11212:9082
            String key ;
            if (currentChild.split("-").length == 2){
                key = currentChild.split("-")[1];
            }else {
                key = currentChild ;
            }
            addCache(key);
        }
    }


    /**
     * 获取所有的服务列表
     *
     * @return
     */
    public List<String> getServerList() {
        List<String> list = new ArrayList<>();

        if (cache.size() == 0) {
            List<String> allNode = zkClient.getChildren(Constant.ROUTE_ZK_ROOT_PATH);
            for (String node : allNode) {
                String key = node.split("-")[1];
                addCache(key);
            }
        }
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }
        logger.info("get all server =[{}] success.", JSON.toJSONString(list));
        return list;

    }

    /**
     * rebuild cache list
     */
    public void rebuildCacheList(){
        updateCache(getServerList()) ;
    }

}
