package com.java.xdd.shiro.mapper;

import com.java.xdd.shiro.pojo.User;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface UserMapper {
    User getUserByUsername(String username);
}
