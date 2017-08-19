package com.java.xdd.shiro.service;

import com.java.xdd.shiro.pojo.Permission;
import com.java.xdd.shiro.pojo.Role;
import com.java.xdd.shiro.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface UserService {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 根据用户名查询用户所有的权限
     * @param id
     * @return
     */
    List<Permission> findPermissionListByUserId(Long id);

    /**
     * 根据用户名查询用户所有的角色
     * @param id
     * @return
     */
    List<Role> findRoleByUserId(Long id);
}
