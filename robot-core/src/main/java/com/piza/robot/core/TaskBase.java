package com.piza.robot.core;

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

    public boolean hasTaskItem(String item){
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
