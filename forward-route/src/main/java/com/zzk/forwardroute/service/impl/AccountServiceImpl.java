package com.zzk.forwardroute.service.impl;

import com.zzk.forwardroute.kit.UIDGenerator;
import com.zzk.forwardroute.pojo.User;
import com.zzk.forwardroute.service.UserService;
import com.zzk.routeapi.vo.req.ChatReqVO;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.res.ZIMServerResVO;
import com.zzk.routeapi.vo.res.RegisterInfoResVO;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.exception.ZIMException;
import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.common.proxy.ProxyManager;
import com.zzk.common.util.RouteInfoParseUtil;
import com.zzk.forwardroute.service.AccountService;
import com.zzk.forwardroute.service.UserInfoCacheService;
import com.zzk.serverapi.ServerApi;
import com.zzk.serverapi.vo.req.SendMsgReqVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.zzk.common.enums.StatusEnum.OFF_LINE;
import static com.zzk.forwardroute.constant.Constant.*;

/**
 * 目前通过Redis直接进行验证，后面可以加入后端登录校验机制
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private OkHttpClient okHttpClient;


    @Autowired
    private UIDGenerator uidGenerator;

    @Autowired
    private UserService userService;

    /**
     * tag 2022年8月16日  暂无使用
     * @param info 用户信息
     * @return
     */
    @Override
    public RegisterInfoResVO register(RegisterInfoResVO info) {
        //tag 2022年8月16日  暂无使用
        String uid = uidGenerator.getUID();
        String key = ACCOUNT_PREFIX + uid;  //key:zim-account:uid  value:token
        //注册到数据库
        User user = new User();
        user.setUid(uid);
        user.setPassword(info.getUserName());
        user.setPassword(info.getPassword());
        userService.addUser(user);


        //这里使用Redis去存注册数据， 不太合适，后面改成注册到数据库中并缓存一份到Redis中
        // 形如  cim-account:{userId}
        String nameVal = redisTemplate.opsForValue().get(info.getUserName());
        return info;
    }


    /**
     * tag 2022年8月16日  暂无使用
     * @param loginReqVO 登录信息
     * @return
     * @throws Exception
     */
    @Override
    public StatusEnum login(LoginReqVO loginReqVO) throws Exception {
        return null;
    }


    /**
     * 保存用户的IM-Server路由信息
     * @param loginReqVO 用户信息
     * @param relatedServerInfo 服务器信息
     * @throws Exception
     */
    @Override
    public void saveRouteInfo(LoginReqVO loginReqVO, String relatedServerInfo) throws Exception {
        String key = ROUTE_PREFIX + loginReqVO.getUserName();
        redisTemplate.opsForValue().set(key, relatedServerInfo);
    }

    /**
     * 加载所有在线用户的IM-Server路由信息
     * @return
     */
    @Override
    public Map<String, ZIMServerResVO> loadRouteRelated() {

        Map<String, ZIMServerResVO> routes = new HashMap<>(64);

        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        //这里只是获取所有匹配的key
        ScanOptions options = ScanOptions.scanOptions()
                .match(ROUTE_PREFIX + "*")
                .build();
        Cursor<byte[]> scan = connection.scan(options);

        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            LOGGER.info("key={}", key);
            parseServerInfo(routes, key);

        }
        try {
            scan.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }

        return routes;
    }

    /**
     * 加载username的IM-Server路由信息
     * @param username
     * @return
     */
    @Override
    public ZIMServerResVO loadRouteRelatedByUsername(String username) {
        String value = redisTemplate.opsForValue().get(ROUTE_PREFIX + username);

        if (value == null) {
            throw new ZIMException(OFF_LINE);
        }

        return new ZIMServerResVO(RouteInfoParseUtil.parse(value));
    }

    private void parseServerInfo(Map<String, ZIMServerResVO> routes, String key) {
        String username = key.split(":")[1];
        String value = redisTemplate.opsForValue().get(key);
        ZIMServerResVO ZIMServerResVO = new ZIMServerResVO(RouteInfoParseUtil.parse(value));
        routes.put(username, ZIMServerResVO);
    }



    @Override
    public void offLine(String username) throws Exception {

        // todo 因为用户可能会直接关闭客户端导致没有正常删除Redis中的登录信息
        //删除路由
        redisTemplate.delete(ROUTE_PREFIX + username);
        LOGGER.info("offLine------>delete ROUTE_PREFIX:[{}]", username);
        //删除token
        redisTemplate.delete(TOKEN_PREFIX + username);
        LOGGER.info("offLine------>delete TOKEN_PREFIX:[{}]", username);
        //删除登录状态
        userInfoCacheService.removeLoginStatus(username);
        //删除username和uid的映射
        redisTemplate.delete(ACCOUNT_PREFIX + username);
        LOGGER.info("offLine------>delete ACCOUNT_PREFIX:[{}]", username);
    }
    @Override
    public void pushMsg(ZIMServerResVO zimServerResVO, String sendUsername, ChatReqVO groupReqVO) throws Exception {

        String url = "http://" + zimServerResVO.getIp() + ":" + zimServerResVO.getHttpPort();
        LOGGER.info(url);
        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMsgReqVO vo = new SendMsgReqVO(sendUsername + ":" + groupReqVO.getMsg(), groupReqVO.getUsername());
        Response response = null;
        try {
            response = (Response) serverApi.sendMsg(vo);
            LOGGER.info("{}", response.body());
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    @Override
    public void pushOfflineMsg(ZIMServerResVO zimServerResVO, ChatReqVO groupReqVO) throws Exception {
        String url = "http://" + zimServerResVO.getIp() + ":" + zimServerResVO.getHttpPort();
        LOGGER.info(url);
        //这里实际可以定义一个新的对象， 由username和token组成， 这里直接复用了ChatReqVO
        String username = groupReqVO.getUsername();
        String token = groupReqVO.getMsg();
        LOGGER.info("{}:{}", username, token);
        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMsgReqVO vo = new SendMsgReqVO();
        vo.setUsername(username);
        vo.setMsg(token);
        Response response = null;
        try {
            response = (Response) serverApi.offlineRepeatUser(vo);
            LOGGER.info("{}", response.body().string());
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

}
