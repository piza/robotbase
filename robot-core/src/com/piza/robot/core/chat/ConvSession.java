package com.piza.robot.core.chat;

import java.util.HashMap;
import java.util.Map;

public class ConvSession {

	private final Map<String,Object> attrMap=new HashMap<String,Object>();
	
	public void setAttribute(String key,Object val){
		attrMap.put(key, val);
	}
	
	public Object getAttribute(String key){
		if(attrMap.containsKey(key)){
			return attrMap.get(key);
		}
		return null;
	}
	
	public void removeAttribute(String key){
		attrMap.remove(key);
	}
	
	
}
