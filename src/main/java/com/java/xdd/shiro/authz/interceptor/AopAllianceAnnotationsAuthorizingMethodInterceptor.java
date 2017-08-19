package com.java.xdd.shiro.authz.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;

import java.util.Collection;

public class AopAllianceAnnotationsAuthorizingMethodInterceptor
extends org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor {

	public AopAllianceAnnotationsAuthorizingMethodInterceptor(){
		super();
		this.methodInterceptors.add(new DataAnnotationMethodInterceptor(new SpringAnnotationResolver()));
		
	}
	
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        org.apache.shiro.aop.MethodInvocation mi = createMethodInvocation(methodInvocation);
        assertAuthorized(mi);
        
        Collection<AuthorizingAnnotationMethodInterceptor> aamis = getMethodInterceptors();
        if (aamis != null && !aamis.isEmpty()) {
            for (AuthorizingAnnotationMethodInterceptor aami : aamis) {
                if (aami.supports(mi)){
                	
                	if(aami instanceof DataAnnotationMethodInterceptor) {
                		return ((DataAnnotationMethodInterceptor)aami).invoke(mi);
                	}
                	
                } 
            }
        }
        
        return super.invoke(mi);
    }

}
