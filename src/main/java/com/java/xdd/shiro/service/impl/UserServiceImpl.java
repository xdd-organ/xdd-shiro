package com.java.xdd.shiro.service.impl;

import com.java.xdd.shiro.pojo.Permission;
import com.java.xdd.shiro.pojo.Role;
import com.java.xdd.shiro.pojo.User;
import com.java.xdd.shiro.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/8/19.
 */
@Service
public class UserServiceImpl implements UserService{

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public List<Permission> findPermissionListByUserId(Long id) {
        return null;
    }

    @Override
    public List<Role> findRoleByUserId(Long id) {
        return null;
    }
}
