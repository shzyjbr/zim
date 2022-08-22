package com.zzk.common.algorithm.consistenthash;


import com.sun.javafx.binding.Logging;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.exception.ZIMException;
import org.apache.log4j.spi.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TreeMap 实现
 */
public class TreeMapConsistentHash extends AbstractConsistentHash {

    private TreeMap<Long, String> treeMap = new TreeMap<Long, String>();
    private static List<String> realNodes = new LinkedList<String>();
    /**
     * 每个真实节点对应的虚拟节点数量
     */
    private static final int VIRTUAL_NODE_SIZE = 5;



    //IM-Server节点下线
    public void remove(String server) {
        //将该节点从真实server列表中移除
        realNodes.remove(server);
        //重新构建虚拟节点
        treeMap.clear();
        for (String s : realNodes) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                String virtualNodeName =  s +"vir"+ i;
                Long hash = super.hash(virtualNodeName);
                treeMap.put(hash, virtualNodeName);
            }
        }
    }

    @Override
    public void add(long key, String value) {
        realNodes.add(value);
        for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
            Long hash = super.hash("vir" + key + i);
            treeMap.put(hash, value);
        }
    }

    public void clear() {
        treeMap.clear();
        realNodes.clear();
    }

    @Override
    public String getFirstNodeValue(String value) {
        long hash = super.hash(value);
        System.out.println("value=" + value + " hash = " + hash);
        SortedMap<Long, String> closest = treeMap.tailMap(hash);
        if (!closest.isEmpty()) {
            return closest.get(closest.firstKey());
        }
        if (treeMap.size() == 0) {

            throw new ZIMException(StatusEnum.SERVER_NOT_AVAILABLE);
        }
        return treeMap.firstEntry().getValue();
    }
}
