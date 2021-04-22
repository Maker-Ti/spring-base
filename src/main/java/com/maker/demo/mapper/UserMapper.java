package com.maker.demo.mapper;

import com.maker.demo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Maker
 * @since 2021-04-18
 */
public interface UserMapper extends BaseMapper<User> {
    User userLogin(User record);
    User getUserByUserName(String username);
}
