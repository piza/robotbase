package com.piza.robot.core;

/**
 * Created by Peter on 2016/10/20.
 */
public abstract class ConversationTask extends TaskBase {

    public abstract ConversationTask newConversation();

    protected final static long DEFAULT_CLOSE_TIME=3600000;

    protected FriendData friendData;
    /**
     * 会话管理
     */
    @Override
    public final void run() {
        if(isNew()){
            this.sendChat("start conversation");
            return;
        }
        String msg=this.chatMessage.getContent();
        if(msg.equals("exit")){
            this.close();
            return;
        }
        this.talk(msg);
    }

    /**
     * sub task don't care conversation, just do work
     * @param msg
     */
    public abstract void talk(String msg);


    public boolean isNew(){
        Conversation conversation= DispatchService.getInstance().getConversation(this.chatMessage.getFriend());
        if(conversation==null){
            conversation=new Conversation();
            conversation.setUserAccount(this.chatMessage.getFriend());
            conversation.setCloseTime(parseCloseTime());
            ConversationTask conversationTask=this.newConversation();
            conversation.setTask(conversationTask);
            DispatchService.getInstance().setConversation(conversation);
            conversationTask.friendData=FriendManage.getInstance().getFriendData(this.chatMessage.getFriend());
            return true;
        }
        return false;
    }

    protected long parseCloseTime(){
        String msg=this.chatMessage.getContent();
        if(msg==null){//default
            return DEFAULT_CLOSE_TIME;
        }
        String[] cmd=msg.split(" ");
        if(cmd.length>1){
            try {
                return Long.parseLong(cmd[1])*60*1000;
            }catch (Exception e){
                return DEFAULT_CLOSE_TIME;
            }
        }else {
            return DEFAULT_CLOSE_TIME;
        }

    }

    protected void close(){
        DispatchService.getInstance().removeConversation(this.chatMessage.getFriend());
        this.sendChat("close conversation");
    }

    protected Integer getUserId(){
        if(friendData!=null){
            return friendData.getUserId();
        }else{
            return 0;
        }
    }
}
