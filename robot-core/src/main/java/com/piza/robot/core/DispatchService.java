package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Peter on 16/9/28.
 */
public class DispatchService {

    private static final Logger logger= Logger.getLogger(DispatchService.class);
    private static volatile DispatchService dispatchService=null;
    private Executor executor;
    private Map<String,Conversation> conversationMap=new ConcurrentHashMap<>();
    private Timer timer=new Timer();

    private DispatchService(){
        executor= Executors.newFixedThreadPool(5);
    }

    public static DispatchService getInstance(){
        if(dispatchService==null){
            synchronized (DispatchService.class){
                if(dispatchService==null) {
                    dispatchService = new DispatchService();
                }
            }
        }
        return dispatchService;
    }


    public synchronized void add(final ChatMessage chatMessage){
        executor.execute(new DispatchTask(chatMessage));
    }

    public synchronized void setConversation(Conversation conversation){
        CloseConversationTask closeConversationTask=new CloseConversationTask(conversation.getTask());
        timer.schedule(closeConversationTask,conversation.getCloseTime());
        conversation.setCloseTask(closeConversationTask);
        this.conversationMap.put(conversation.getUserAccount(),conversation);
    }

    public Conversation getConversation(String account){
        if(conversationMap.containsKey(account)){
            return conversationMap.get(account);
        }else {
            return null;
        }
    }

    public void removeConversation(String account){
        if(conversationMap.containsKey(account)){
            Conversation conversation= conversationMap.remove(account);
            try{
                conversation.getCloseTask().cancel();
            }catch (Exception e){

            }
        }
    }

    public class DispatchTask implements Runnable{

        private ChatMessage chatMessage;
        public DispatchTask(ChatMessage chatMessage){
            this.chatMessage=chatMessage;
        }
        @Override
        public void run() {
            Collection<IAnalyser> allAnalyser=ParserManage.getInstance().getAllAnalyser();
            boolean foundTask=false;
            for(IAnalyser iAnalyser:allAnalyser){
               Analysis analysis= iAnalyser.analyse(this.chatMessage.getContent());
                if(analysis.isHandleable()){
                    TaskBase taskBase=TaskManager.getInstance().getTask(iAnalyser.getTaskName());
                    taskBase.setChatMessage(chatMessage);
                    if(System.currentTimeMillis()-Launcher.startTime<3000){//fix bug of history task issue
                        logger.info("drop history task:"+taskBase.getTaskName());
                        taskBase.sendChat("old command, ignored!");
                        break;
                    }
                    TaskService.getInstance().addTask(taskBase);
                    foundTask=true;
                    if(!analysis.isContinuable()){
                        break;
                    }
                }

            }
            if(!foundTask){
                TaskBase defaultTask=TaskManager.getInstance().getTask("defaultTask");
                defaultTask.setChatMessage(chatMessage);
                TaskService.getInstance().addTask(defaultTask);
            }
        }
    }


    private class CloseConversationTask extends TimerTask{

        private ConversationTask conversationTask;

        public CloseConversationTask(ConversationTask conversationTask){
            this.conversationTask=conversationTask;
        }
        @Override
        public void run() {
            try {
                conversationTask.close();
            }catch (Exception e){

            }
        }
    }

}
