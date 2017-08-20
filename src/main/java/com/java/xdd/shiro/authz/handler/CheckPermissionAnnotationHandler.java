package com.java.xdd.shiro.authz.handler;

import com.java.xdd.shiro.authz.annotation.CheckPermission;
import com.java.xdd.shiro.mapper.CheckPermissionMapper;
import com.java.xdd.shiro.service.CheckPermissionService;
import com.java.xdd.shiro.service.impl.CheckPermissionServiceImpl;
import javafx.application.Application;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

public class CheckPermissionAnnotationHandler extends AuthorizingAnnotationHandler {

	@Autowired
	private CheckPermissionServiceImpl checkPermissionService;

	public CheckPermissionAnnotationHandler() {
		super(CheckPermission.class);
//		super(annotationClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void assertAuthorized(Annotation annotation) throws AuthorizationException {
		// TODO Auto-generated method stub
		// RequiresAction rpAnnotation = (RequiresAction)a;
		// do nothing
		Subject subject = SecurityUtils.getSubject();
		System.out.println("CheckPermission");
		Object principal = subject.getPrincipal();
		PrincipalCollection principals = subject.getPrincipals();
		Class<? extends Annotation> annotationClass = annotation.annotationType();
		if (annotation instanceof CheckPermission) {
			CheckPermission checkPermission = (CheckPermission) annotation;
			String tableName = checkPermission.tableName();
			String[] fieldNames = checkPermission.fieldNames();
			String[] fieldValues = checkPermission.fieldValues();
			boolean loginUser = checkPermission.isLoginUser();
			String[] queryFields = checkPermission.queryFields();
			String[] queryValues = checkPermission.queryValues();
			System.out.println("CheckPermission");
//			ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
//			CheckPermissionService bean = app.getBean(CheckPermissionService.class);
//			CheckPermissionMapper bean1 = app.getBean(CheckPermissionMapper.class);
			Map<String, Object> oneByCheckPermission = checkPermissionService.getOneByCheckPermission(tableName, queryFields, queryValues);
			System.out.println(oneByCheckPermission);
		}


	}

}
