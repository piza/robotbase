package com.piza.robot.core.chat;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;


public class GtalkUserResponse implements UserResponse{

	private final Map<String,Object> attributes=new HashMap<String,Object>();
    Logger log=Logger.getLogger(GtalkUserResponse.class);
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

	@Override
	public void response(String text) {
		if(this.attributes.containsKey(Constants.GTALK_CHAT)){
			Chat chat=(Chat)this.attributes.get(Constants.GTALK_CHAT);
			try {
				chat.sendMessage(text);
			} catch (SmackException.NotConnectedException e) {
                log.error("Encounter exception when return message to user: \n"+e.getMessage());
                e.printStackTrace();
            }
        }else{
			log.error("There is no "+Constants.GTALK_CHAT +" attribute in GtalkUserResponse!");
		}
		
	}
	
	
	
}
