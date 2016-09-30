package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Peter on 16/9/28.
 */
public class TaskService {

    private static final Logger logger= Logger.getLogger(TaskService.class);
    private static volatile TaskService taskService=null;
    private static volatile int taskLong=0;
    private Executor executor;

    private TaskService(){
        executor= Executors.newFixedThreadPool(5);
    }

    public static TaskService getInstance(){
        if(taskService==null){
            synchronized (TaskService.class){
                if(taskService==null) {
                    taskService = new TaskService();
                }
            }
        }
        return taskService;
    }

    public String report(){
        StringBuilder sb=new StringBuilder();
        sb.append("task service\n");
        sb.append("task executed:"+taskLong);
        return sb.toString();
    }

    public synchronized void addTask(TaskBase taskBase){
        logger.info("add task:"+taskBase.getTaskName());
        taskLong++;
        executor.execute(taskBase);
    }
}
