package com.zzk.forwardroute.service;

import com.zzk.common.pojo.ZIMUserInfo;

import java.util.Set;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/24 11:06
 * @since JDK 1.8
 */
public interface UserInfoCacheService {

    /**
     * 通过 userID 获取用户信息
     *
     * @param username 用户唯一 ID
     * @return
     * @throws Exception
     */
    ZIMUserInfo loadUserInfoByUsername(String username) ;

    /**
     * 保存和检查用户登录情况
     * @return true:未登录，允许登录    false:已经登录，发生重复登录
     * @throws Exception
     */
    boolean saveUserLoginStatus(String username) throws Exception ;

    /**
     * 清除用户的登录状态
     * @param username
     * @throws Exception
     */
    void removeLoginStatus(String username) throws Exception ;


    /**
     * query all online user
     * @return online user
     */
    Set<ZIMUserInfo> onlineUser() ;
}
