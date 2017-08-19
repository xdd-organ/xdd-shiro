package com.java.xdd.shiro.authz.interceptor;

import com.java.xdd.shiro.authz.annotation.CheckPermission;
import com.java.xdd.shiro.authz.annotation.RequiresData;
import com.java.xdd.shiro.authz.domain.DataParameterRequest;
import com.java.xdd.shiro.authz.handler.CheckPermissionAnnotationHandler;
import com.java.xdd.shiro.authz.handler.DataAnnotationHandler;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckPermissionAnnotationMethodInterceptor
        extends AuthorizingAnnotationMethodInterceptor {

    public CheckPermissionAnnotationMethodInterceptor(){
        super(new DataAnnotationHandler());
    }
    public CheckPermissionAnnotationMethodInterceptor(AnnotationResolver resolver){
        super(new CheckPermissionAnnotationHandler(),resolver);
    }


    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        // TODO Auto-generated method stub
        try {
            CheckPermissionAnnotationHandler handler = (CheckPermissionAnnotationHandler) getHandler();
            handler.assertAuthorized(this.getAnnotation(mi));
        } catch(AuthorizationException ae) {
            // Annotation handler doesn't know why it was called, so add the information here if possible.
            // Don't wrap the exception here since we don't want to mask the specific exception, such as
            // UnauthenticatedException etc.
            if (ae.getCause() == null) ae.initCause(new AuthorizationException("Not authorized to invoke method: " + mi.getMethod()));
            throw ae;
        }

    }


    /**
     * 控制是否可以访问
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // TODO Auto-generated method stub
        assertAuthorized(methodInvocation);

        //待调用的目标对象及参数列表
        Object obj = methodInvocation.getThis();
        Object[] args = methodInvocation.getArguments();

        //当前声明的注解
        CheckPermission an = (CheckPermission)this.getAnnotation(methodInvocation);

        //当前登录用户
        Object principal = this.getSubject().getPrincipal();
        Class<?> clz = principal.getClass();


        return methodInvocation.proceed();
        //继续调用目标类的目标方法，传入修改过的参数数组
        //return methodInvocation.getMethod().invoke(obj, args);
    }


}
