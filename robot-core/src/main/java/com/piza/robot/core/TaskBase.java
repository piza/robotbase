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

    public abstract String getTaskName();
}
