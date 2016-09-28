package com.piza.robot.core.chat;

import com.piza.robot.core.IAnalyser;
import org.jivesoftware.smack.packet.Message;


public class GtalkDispatcher extends AbstractDispatcher{

	
	
	public GtalkDispatcher(){
		super();
	}
	
	@Override
	public void localHandleMessage(UserRequest request,UserResponse response){
		Message mes=(Message)request.getAttribute(Constants.GTALK_MESSAGE);
		String text=mes.getBody();
		String controllerName=null;
		boolean handleable=false; 
		for(IAnalyser anlysis:this.allAnalyser.values()){
			if(anlysis.analyse(text).isHandleable()){
				controllerName=anlysis.getTaskName();
				handleable=true;
				break;
			}
		}
		if(!handleable){
			response.response("Sorry,I don't understand \""+text+"\"");
		}
		if(controllerName!=null && !controllerName.equals("")){
//			BaseController cont=(BaseController)ConvUtil.getSpringContext().getBean(controllerName);
//			cont.setRequest(request);
//			ProcessResult res=cont.processMessage(text);
//			BaseView view=(BaseView)ConvUtil.getSpringContext().getBean(res.getViewName());
//			response.response(view.getResult(res));
		}
	}
	
}
