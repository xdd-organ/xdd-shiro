package com.java.xdd.shiro.realm;

import com.java.xdd.shiro.pojo.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户自定义凭证匹配器
 */
public class CustomerHashedCredentialsMatcher extends HashedCredentialsMatcher{

    private Logger logger = LoggerFactory.getLogger(CustomerHashedCredentialsMatcher.class);

    /**
     *
     * @param token 页面传来的用户信息
     * @param info 数据库查询的信息
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //AuthenticationToken   账号+密码
        String password = null;
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken token1 = (UsernamePasswordToken) token;
            char[] password1 = token1.getPassword();
            password = new String(password1);
        }

        //AuthenticationInfo --> simpleAuthenticationInfo(实现)
        if (info instanceof SimpleAuthenticationInfo) {
            SimpleAuthenticationInfo simpleInfo = (SimpleAuthenticationInfo) info;
            PrincipalCollection principals = info.getPrincipals();
            Object principal = principals.getPrimaryPrincipal();
            if (principal instanceof User) {
                return true;
            }
        }
        return false;
    }

}
