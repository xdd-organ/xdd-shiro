package com.java.xdd.shiro.authz.domain;

import java.util.HashMap;
import java.util.Map;

public class ConditionRequest implements DataParameterRequest {
	
	public String author;
	
	private Map<String,String> parameters = new HashMap<String,String>();
	
	@Override
	public Map<String, String> getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}
	
	public void setParameters(Map<String,String> p){
		this.parameters=p;
	}

}
