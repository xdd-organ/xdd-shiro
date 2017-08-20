package com.java.xdd.shiro.realm;

import com.java.xdd.shiro.pojo.Permission;
import com.java.xdd.shiro.pojo.Role;
import com.java.xdd.shiro.pojo.User;
import com.java.xdd.shiro.service.UserService;
import com.java.xdd.shiro.service.impl.CheckPermissionServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CustomRealm extends AuthorizingRealm {
    /***/
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private CheckPermissionServiceImpl checkPermissionService;

    @Override
    public void setName(String name) {
        super.setName("customRealm"); // 设置realm的名称
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = null;
        try {
            //获取数据库总的用户
            user = userService.getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user == null) return null;

        String password = user.getPassword();
        final User userInfo = new User();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());

        //构建一个userInfo
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                userInfo, password, ByteSource.Util.bytes(user.getSalt()), this.getName());
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //从 principals获取主身份信息
        //将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
        User sysUser =  (User) principals.getPrimaryPrincipal();

        //根据身份信息获取权限信息
        //从数据库获取到权限数据
        List<Permission> permissionList = null;
        List<Role> roleList = null;
        try {
            permissionList = userService.findPermissionListByUserId(sysUser.getId());
            roleList = userService.findRoleByUserId(sysUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //单独定一个集合对象
        List<String> permissions = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)){
            for (Permission sysPermission : permissionList){
                //将数据库中的权限标签 符放入集合
                permissions.add(sysPermission.getCode());
            }
        }
        if (!CollectionUtils.isEmpty(roleList)){
            for (Role role : roleList){
                //将数据库中的角色 符放入集合
                roles.add(role.getCode());
            }
        }

        //查到权限数据，返回授权信息(要包括 上边的permissions)
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        authorizationInfo.addStringPermissions(permissions); //添加访问请求

        //simpleAuthorizationInfo.addObjectPermissions(null);
        authorizationInfo.addRoles(roles); //添加角色
        return authorizationInfo;
    }


}
