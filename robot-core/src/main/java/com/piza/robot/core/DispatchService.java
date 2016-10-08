package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Peter on 16/9/28.
 */
public class DispatchService {

    private static final Logger logger= Logger.getLogger(DispatchService.class);
    private static volatile DispatchService dispatchService=null;
    private Executor executor;

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
                    if(System.currentTimeMillis()-Launcher.startTime<3000){
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

}
