package com.piza.robot.core.chat;

public interface UserResponse {
	
	public Object getAttribute(String key);
	public void setAttribute(String key, Object value);
	
	public void response(String text);

}
