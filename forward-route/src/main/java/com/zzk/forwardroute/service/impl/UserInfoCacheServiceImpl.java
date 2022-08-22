package com.zzk.forwardroute.service.impl;

import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.forwardroute.service.UserInfoCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.zzk.forwardroute.constant.Constant.*;

@Slf4j
@Service
public class UserInfoCacheServiceImpl implements UserInfoCacheService {

    /**
     * todo 本地缓存，为了防止内存撑爆，后期可换为 LRU。
     */
    private final static Map<String, ZIMUserInfo> USER_INFO_MAP = new ConcurrentHashMap<>(64);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ZIMUserInfo loadUserInfoByUsername(String username) {

        //优先从本地缓存获取
        ZIMUserInfo zimUserInfo = USER_INFO_MAP.get(username);
        if (zimUserInfo != null) {
            return zimUserInfo;
        }

        //load redis
        String uid = redisTemplate.opsForValue().get(ACCOUNT_PREFIX + username);
        if (uid != null) {
            zimUserInfo = new ZIMUserInfo(uid, username);
            USER_INFO_MAP.put(username, zimUserInfo);
        }

        return zimUserInfo;
    }

    @Override
    public boolean saveUserLoginStatus(String username) throws Exception {
        //向redis的login-status集合中添加登录状态
        Long add = redisTemplate.opsForSet().add(LOGIN_STATUS_PREFIX, username);
        if (add == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void removeLoginStatus(String username) throws Exception {
        log.info("removeLoginStatus----> remove [{}] login status", username);
        redisTemplate.opsForSet().remove(LOGIN_STATUS_PREFIX, username);
    }

    @Override
    public Set<ZIMUserInfo> onlineUser() {
        Set<ZIMUserInfo> set = null;
        Set<String> members = redisTemplate.opsForSet().members(LOGIN_STATUS_PREFIX);
        for (String member : members) {
            if (set == null) {
                set = new HashSet<>(64);
            }
            ZIMUserInfo zimUserInfo = loadUserInfoByUsername(member);
            set.add(zimUserInfo);
        }

        return set;
    }

}
