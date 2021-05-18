package com.maker.demo.service;

import com.maker.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Maker
 * @since 2021-04-18
 */
public interface UserService extends IService<User> {
    User userLogin(User record);
    User getUserByUserName(String username);
    List<User> getFollowingUser(String query);
}
