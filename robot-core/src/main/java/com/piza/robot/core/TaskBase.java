package com.piza.robot.core;

import java.util.HashMap;
import java.util.Map;

/** abstract task
 * Created by Peter on 16/9/28.
 */
public abstract class TaskBase implements Runnable{


    protected ChatMessage chatMessage;

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void sendChat(String msg){
        ChatService.getInstance().sendMessage(this.chatMessage.getFriend(),msg);
    }

    public abstract String getTaskName();

    public String getMsgContent(){
        if(chatMessage==null){
            return "";
        }
        return chatMessage.getContent();
    }

    public void overwriteItem(String item,Boolean value){
        if(chatMessage==null){
            return;
        }
        chatMessage.overwriteItem.put(item,value);
    }
    public boolean hasTaskItem(String item){
        if(chatMessage!=null && chatMessage.overwriteItem.containsKey(item)){
            return chatMessage.overwriteItem.get(item);
        }
        String cmdStr=this.getMsgContent();
        if(cmdStr==null || !cmdStr.contains(" ")){
            return false;
        }

        String[] itemArr=cmdStr.split(" ");
        for(String itemStr:itemArr){
            if(itemStr.equalsIgnoreCase(item)){
                return true;
            }

        }

        return false;
    }
}
