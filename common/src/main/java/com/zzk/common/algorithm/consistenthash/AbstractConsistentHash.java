package com.zzk.common.algorithm.consistenthash;

import java.util.List;

/**
 * Function:一致性 hash 算法抽象类
 *
 * @author crossoverJie
 * Date: 2019-02-27 00:35
 * @since JDK 1.8
 */
public abstract class AbstractConsistentHash {

    /**
     * 新增节点
     * @param key
     * @param value
     */
    protected abstract void add(long key,String value);

    protected abstract void clear();

//    protected abstract void clearServers();

    /**
     * 排序节点，数据结构自身支持排序可以不用重写
     */
    protected void sort(){}

    /**
     * 根据当前的 key 通过一致性 hash 算法的规则取出一个节点
     * @param value
     * @return
     */
    protected abstract String getFirstNodeValue(String value);

    /**
     * 传入节点列表以及客户端信息获取一个IM-Server
     * @param servers IM-Server 列表
     * @param key
     * @return
     */
    public String process(List<String> servers,String key){
        clear();
        for (String s : servers) {
            add(hash(s), s);
        }
        sort();
        //clearServers();

        return getFirstNodeValue(key) ;
    }

    /**
     * hash 运算  FNV1_32_HASH算法
     * @return
     */
    public Long hash(String str){
        final long p = 16777619;
        long hash = 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
