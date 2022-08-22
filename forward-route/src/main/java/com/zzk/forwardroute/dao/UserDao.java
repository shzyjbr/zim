package com.zzk.forwardroute.dao;

import com.zzk.forwardroute.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zzk
 * @date 2022/8/14 19:22
 * @desctiption
 */
@Mapper
@Repository
public interface UserDao {
    List<User> getAllUsers();

    boolean addUser(User user);

    User findUserByUID(@Param("uid")  String uid);

    User findUserByUsername(@Param("username") String username);
}
