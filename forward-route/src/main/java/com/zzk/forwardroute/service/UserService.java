package com.zzk.forwardroute.service;

import com.github.pagehelper.PageInfo;
import com.zzk.forwardroute.pojo.User;
import com.zzk.forwardroute.query.UserQuery;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.res.LoginResVO;

/**
 * @author zzk
 * @date 2022/8/14 20:23
 * @desctiption
 */
public interface UserService {
    PageInfo<User> getAllUsers(UserQuery userQuery);
    boolean addUser(User user);

    User findUserByUID(String uid);

    User findUserByUsername(String username);

    LoginResVO validateUser(LoginReqVO login);
}
