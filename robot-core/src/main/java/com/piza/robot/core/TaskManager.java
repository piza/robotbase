package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Peter on 16/9/28.
 */
public class TaskManager {

    private static final Logger logger= Logger.getLogger(TaskManager.class);

    private static volatile TaskManager taskManager=null;

    private Map<String,TaskBase> taskPool=new ConcurrentHashMap<>();

    private TaskManager(){}
    public static TaskManager getInstance(){
        if(taskManager==null){
            synchronized (TaskManager.class){
                if(taskManager==null) {
                    taskManager = new TaskManager();
                }
            }
        }
        return taskManager;
    }

    public void addTask(TaskBase taskBase){
        logger.info("add task:"+taskBase.getTaskName());
        taskPool.put(taskBase.getTaskName(),taskBase);
    }

    public TaskBase getTask(String key){
        TaskBase task=taskPool.get(key);
        if(task!=null){
            logger.info("get task:"+task.getTaskName());
        }
        return task;
    }

    public String report(){
        StringBuilder sb=new StringBuilder();
        sb.append("all task\n");
        for(String taskName:taskPool.keySet()){
            sb.append(taskName).append(",");
        }
        sb.append("\n");
        return sb.toString();
    }

}
