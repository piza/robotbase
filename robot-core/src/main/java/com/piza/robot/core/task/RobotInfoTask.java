package com.piza.robot.core.task;

import com.piza.robot.core.*;

/**
 * Created by Peter on 16/9/29.
 */
public class RobotInfoTask  extends TaskBase {
    @Override
    public String getTaskName() {
        return "robotInfoTask";
    }

    @Override
    public void run() {
        String sendMsg="status report:\n"
                +getSystemInfo()
                +ParserManage.getInstance().report()
                +TaskManager.getInstance().report()
                +TaskService.getInstance().report()+"\n"
                +ChatService.getInstance().report();
        ChatService.getInstance().sendMessage(this.chatMessage.getFriend(),sendMsg);
    }

    private String getSystemInfo(){
        return "freeMemory:"+Runtime.getRuntime().freeMemory()+",maxMemory:"+Runtime.getRuntime().maxMemory()
                +",totalMemory:"+Runtime.getRuntime().totalMemory()+"\n";
    }
}
