package com.java.xdd.shiro.authz.handler;

import com.java.xdd.shiro.authz.annotation.CheckPermission;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import java.lang.annotation.Annotation;

public class CheckPermissionAnnotationHandler extends AuthorizingAnnotationHandler {

	public CheckPermissionAnnotationHandler() {
		super(CheckPermission.class);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void assertAuthorized(Annotation a) throws AuthorizationException {
		// TODO Auto-generated method stub
		// RequiresAction rpAnnotation = (RequiresAction)a;
		// do nothing
	}

}
