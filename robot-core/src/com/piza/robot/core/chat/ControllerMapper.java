package com.piza.robot.core.chat;

import java.util.Map;



public class ControllerMapper {

	private Map<String,Object> allController;
	
	public ControllerMapper(){
//		this.allController=ConvUtil.getSpringContext().getBeansWithAnnotation(Controller.class);
	}

	public Map<String, Object> getAllController() {
		return allController;
	}

	
}
