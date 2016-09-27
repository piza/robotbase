package com.piza.robot.core.chat;

import java.util.HashMap;
import java.util.Map;


public class GtalkUserRequest implements UserRequest{
	
	private final Map<String,Object> attributes=new HashMap<String,Object>();

	@Override
	public Object getAttribute(String key) {
		if(attributes.containsKey(key)){
		return attributes.get(key);
		}
		return null;
	}

	@Override
	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
		
	}
	
	

}
