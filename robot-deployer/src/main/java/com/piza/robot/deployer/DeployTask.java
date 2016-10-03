package com.piza.robot.deployer;

import com.piza.robot.core.ChatService;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployTask extends TaskBase {


    @Override
    public String getTaskName() {
        return "deployTask";
    }

    @Override
    public void run() {

        String sendMsg="start task:"+this.chatMessage.getContent();
        ChatService.getInstance().sendMessage(this.chatMessage.getFriend(),sendMsg);

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         sendMsg="end task of deploy";
        ChatService.getInstance().sendMessage(this.chatMessage.getFriend(),sendMsg);

    }
}
