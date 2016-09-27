package com.piza.robot.core.chat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class AbstractDispatcher implements Runnable{

	protected final ControllerMapper ctrlMapper=new ControllerMapper();
	protected final ViewMapper viewMapper=new ViewMapper();
	protected final Map<String,IAnalyser> allAnalyser=new HashMap<String,IAnalyser>();
	private UserRequest request;
	private UserResponse response;
	Logger log=Logger.getLogger(ControllerMapper.class);
	protected AbstractDispatcher(){
		initAnalyser();
	}
	
	protected void initAnalyser(){
		
		Map<String,Object> allCtrl=ctrlMapper.getAllController();
		Iterator<String> ite=allCtrl.keySet().iterator();
		while(ite.hasNext()){
			String key=ite.next();
			allAnalyser.put(key, ((IAssistor)(allCtrl.get(key))).getAnalyser());
		}
	}
	
	
	@Override
	public void run() {
        this.handleMessage(request, response);
	}
	
	public final void handleMessage(UserRequest request,UserResponse response){
		localHandleMessage(request,response);
	}
	
	public abstract void localHandleMessage(UserRequest request,UserResponse resonse);

	public UserRequest getRequest() {
		return request;
	}

	public void setRequest(UserRequest request) {
		this.request = request;
	}

	public UserResponse getResponse() {
		return response;
	}

	public void setResponse(UserResponse response) {
		this.response = response;
	}

}
