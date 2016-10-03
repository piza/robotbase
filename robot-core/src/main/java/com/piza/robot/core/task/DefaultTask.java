package com.piza.robot.core.task;

import com.piza.robot.core.*;

/**
 * Created by Peter on 16/9/29.
 */
public class DefaultTask extends TaskBase {
    @Override
    public String getTaskName() {
        return "defaultTask";
    }

    @Override
    public void run() {
        String sendMsg="sorry,can't understand u!";
        ChatService.getInstance().sendMessage(this.chatMessage.getFriend(),sendMsg);
    }
}
