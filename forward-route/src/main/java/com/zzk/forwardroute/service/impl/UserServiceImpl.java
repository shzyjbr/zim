package com.zzk.forwardroute.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzk.common.util.StringUtil;
import com.zzk.forwardroute.dao.UserDao;
import com.zzk.forwardroute.pojo.User;
import com.zzk.forwardroute.query.UserQuery;
import com.zzk.forwardroute.service.UserService;
import com.zzk.routeapi.vo.req.LoginReqVO;
import com.zzk.routeapi.vo.res.LoginResVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzk
 * @date 2022/8/14 19:48
 * @desctiption
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public PageInfo<User> getAllUsers(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPageNum(), userQuery.getPageSize());
        return new PageInfo<>(userDao.getAllUsers());

    }

    @Override
    public boolean addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User findUserByUID(String uid) {
        return userDao.findUserByUID(uid);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public LoginResVO validateUser(LoginReqVO login) {
        User userByUsername = userDao.findUserByUsername(login.getUserName());
        if (userByUsername == null)
            return null;
        if (!StringUtils.equals(login.getPassword(), userByUsername.getPassword()))
            return null;
        LoginResVO loginResVO = new LoginResVO();
        loginResVO.setUid(userByUsername.getUid());
        loginResVO.setUsername(userByUsername.getUsername());
        return loginResVO;
    }
}
