package com.zzk.forwardroute.controller;

import com.github.pagehelper.PageInfo;
import com.zzk.common.util.JWTUtils;
import com.zzk.forwardroute.kit.UIDGenerator;
import com.zzk.forwardroute.pojo.User;
import com.zzk.forwardroute.query.UserQuery;
import com.zzk.forwardroute.service.UserService;
import com.zzk.routeapi.RouteApi;
import com.zzk.routeapi.vo.req.ChatReqVO;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.req.P2PReqVO;
import com.zzk.routeapi.vo.req.RegisterInfoReqVO;
import com.zzk.routeapi.vo.res.LoginResVO;
import com.zzk.routeapi.vo.res.ZIMServerResVO;
import com.zzk.common.algorithm.RouteHandle;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.exception.ZIMException;
import com.zzk.common.pojo.RouteInfo;
import com.zzk.common.pojo.ZIMUserInfo;
import com.zzk.common.res.BaseResponse;
import com.zzk.common.res.NULLBody;
import com.zzk.common.util.RouteInfoParseUtil;
import com.zzk.forwardroute.cache.ServerCache;
import com.zzk.forwardroute.service.AccountService;
import com.zzk.forwardroute.service.CommonBizService;
import com.zzk.forwardroute.service.UserInfoCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.zzk.forwardroute.constant.Constant.*;

/**
 * ForwardRoute 负责处理客户端的登录、注册、下线
 */
@Controller
@RequestMapping()
public class RouteController implements RouteApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private ServerCache serverCache;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private CommonBizService commonBizService;

    @Autowired
    private RouteHandle routeHandle;

    @Autowired
    private UserService userService;

    @Autowired
    private UIDGenerator uidGenerator;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;



    /**
     * 客户端下线
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "offLine", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<NULLBody> offLine(@RequestBody ChatReqVO groupReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse<>();

//        ZIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUsername(groupReqVO.getUsername());
        String username = groupReqVO.getUsername();
        LOGGER.info("user [{}] offline!", username);
        accountService.offLine(groupReqVO.getUsername());

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 登录并分配给客户端一台 server
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<LoginResVO> login(@RequestBody LoginReqVO loginReqVO) throws Exception {
        BaseResponse<LoginResVO> res = new BaseResponse<>();
        //校验用户信息
        LoginResVO loginResVO = userService.validateUser(loginReqVO);
        if (loginResVO == null){
            res.setCode(StatusEnum.ERROR_USERNAME_OR_PASSWORD.getCode());
            res.setMessage(StatusEnum.REPEAT_USERNAME.getMessage());
            return res;
        }
        String uid = loginResVO.getUid();
        String username = loginReqVO.getUserName();
        //查询是否已经有该用户登录，有的话是重复登录，会挤掉原先的登录用户
        String tokenInRedis = redisTemplate.opsForValue().get(TOKEN_PREFIX+username);
        Set<String> onlineUsers = redisTemplate.opsForSet().members(LOGIN_STATUS_PREFIX);
        //todo 切换IM-Server时有BUG 原来的IM-Server因为故障下线了，但是现在路由关系中还是指向这个IM-Server
        //todo 结果就是向这个故障的IM-Server发送用户下线通知，但此时IM-Server已经不可达
        if (onlineUsers != null) {
            for(String onlineUser : onlineUsers) {
                if (onlineUser.equals(username)) {
                    //查询其路由关系
                    ZIMServerResVO zimServerResVO = accountService.loadRouteRelatedByUsername(username);
                    ChatReqVO chatVO = new ChatReqVO(username, tokenInRedis);
                    //向该IM-Server发送用户下线通知
                    LOGGER.info("accountService.pushOfflineMsg: {}, user:[{}]", zimServerResVO, username);
                    accountService.pushOfflineMsg(zimServerResVO ,chatVO);
                    //清除旧的路由、登录状态、token
//                    accountService.offLine(username);
                }
            }
        }
        //使用用户名进行路由
        String server = commonBizService.fetchServer(username);
        LOGGER.info("userName=[{}] route server =[{}]", loginReqVO.getUserName(), server);
        RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
        //根据用户名保存路由信息
        accountService.saveRouteInfo(loginReqVO, server);
        //生成token 并存入redis
        JWTUtils jwt = JWTUtils.getInstance();
        String token = jwt
                .setClaim("uid", uid)
                .setExpired(30 * 60 * 1000)
                .setClaim("username", username)
                .generateToken();
        String key = TOKEN_PREFIX + username; //设置username查看比较直观，也可以设置uid
        redisTemplate.opsForValue().set(key, token,30 * 60, TimeUnit.SECONDS);


        //保存username-uid的映射
        redisTemplate.opsForValue().set(ACCOUNT_PREFIX+username, uid);
        //保存登录状态
        userInfoCacheService.saveUserLoginStatus(username);
        ZIMServerResVO routedServer = new ZIMServerResVO(routeInfo);
        loginResVO.setUid(uid);
        loginResVO.setRoutedServer(routedServer);
        loginResVO.setToken(token);

        res.setDataBody(loginResVO);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 注册账号, 发送账号密码
     * username
     * password
     *
     * @return
     */
    @RequestMapping(value = "registerAccount", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<Object> registerAccount(@RequestBody RegisterInfoReqVO req) throws Exception {
        BaseResponse<Object> res = new BaseResponse<>();

        User user = new User();
        String username = req.getUserName();
        String password = req.getPassword();
        user.setPassword(password);
        user.setUsername(username);
        //查询是否存在同名用户， 用户名不可重复
        User queryUser = userService.findUserByUsername(username);
        if (queryUser != null) {
            //返回失败信息
            res.setCode(StatusEnum.REPEAT_USERNAME.getCode());
            res.setMessage(StatusEnum.REPEAT_USERNAME.getMessage());

        } else {
            user.setUid(uidGenerator.getUID());
            userService.addUser(user);
            res.setCode(StatusEnum.SUCCESS.getCode());
            res.setMessage(StatusEnum.SUCCESS.getMessage());
        }
        res.setDataBody("");
        return res;
    }

    /**
     * 获取所有在线用户
     *
     * @return
     */
    @RequestMapping(value = "onlineUser", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<Set<ZIMUserInfo>> onlineUser() throws Exception {
        BaseResponse<Set<ZIMUserInfo>> res = new BaseResponse<>();

        Set<ZIMUserInfo> cimUserInfos = userInfoCacheService.onlineUser();
        res.setDataBody(cimUserInfos);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


    /**
     * 私聊
     * @param p2pRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "p2pRoute", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<NULLBody> p2pRoute(@RequestBody P2PReqVO p2pRequest) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        try {
            //获取接收消息用户的路由信息
            ZIMServerResVO cimServerResVO = accountService.loadRouteRelatedByUsername(p2pRequest.getReceiveUsername());

            //p2pRequest.getReceiveUserId()==>消息接收者的 userID
            ChatReqVO chatVO = new ChatReqVO(p2pRequest.getReceiveUsername(),p2pRequest.getMsg()) ;
            accountService.pushMsg(cimServerResVO ,p2pRequest.getUsername(),chatVO);

            res.setCode(StatusEnum.SUCCESS.getCode());
            res.setMessage(StatusEnum.SUCCESS.getMessage());

        }catch (ZIMException e){
            res.setCode(e.getErrorCode());
            res.setMessage(e.getErrorMessage());
        }
        return res;
    }


    /**
     * 群聊
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "groupRoute", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<NULLBody> groupRoute(@RequestBody ChatReqVO groupReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        LOGGER.info("msg=[{}]", groupReqVO.toString());

        //获取所有的推送列表
        Map<String, ZIMServerResVO> serverResVOMap = accountService.loadRouteRelated();
        for (Map.Entry<String, ZIMServerResVO> cimServerResVOEntry : serverResVOMap.entrySet()) {
            String username = cimServerResVOEntry.getKey();
            ZIMServerResVO zimServerResVO = cimServerResVOEntry.getValue();
            if (username.equals(groupReqVO.getUsername())){
                //过滤掉自己
                ZIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUsername(groupReqVO.getUsername());
                LOGGER.warn("过滤掉了发送者 userId={}",cimUserInfo.toString());
                continue;
            }

            //推送消息
            ChatReqVO chatVO = new ChatReqVO(username,groupReqVO.getMsg()) ;
            accountService.pushMsg(zimServerResVO ,groupReqVO.getUsername(),chatVO);

        }

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


    @GetMapping("addUser")
    @ResponseBody
    public String addUser(User user) {
        boolean res = userService.addUser(user);
        return res ? "OK" : "fail";
    }

    @GetMapping("listUser")
    @ResponseBody
    public PageInfo<User> getAllUsers() {
        UserQuery query = new UserQuery();
        query.setPageNum(0);
        query.setPageSize(5);
        PageInfo<User> res = userService.getAllUsers(query);
        return res;
    }

    @GetMapping("findUserByUID")
    @ResponseBody
    public User findUserByUID(@RequestParam String uid) {
        User res = userService.findUserByUID(uid);
        return res;
    }

    @GetMapping("findUserByUsername")
    @ResponseBody
    public User findUserByUsername(@RequestParam String username) {
        User res = userService.findUserByUsername(username);
        return res;
    }

}
