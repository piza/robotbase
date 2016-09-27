package com.piza.robot.core.chat;

import java.util.HashMap;
import java.util.Map;

public class ProcessResult {

	private String viewName;
	private Map<String,Object> allValues=new HashMap<String,Object>();
	
	public void addValue(String key,Object val){
		this.allValues.put(key, val);
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	public Object getValue(String key){
		return this.allValues.get(key);
	}
	
	
}
