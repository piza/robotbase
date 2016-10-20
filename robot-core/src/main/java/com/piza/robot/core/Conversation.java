package com.piza.robot.core;

import java.util.TimerTask;

/**
 * Created by Peter on 2016/10/20.
 */
public class Conversation {

    protected String userAccount;
    protected Long closeTime;
    protected ConversationTask task;
    protected boolean isFirst;
    protected TimerTask closeTask;


    public TimerTask getCloseTask() {
        return closeTask;
    }

    public void setCloseTask(TimerTask closeTask) {
        this.closeTask = closeTask;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public ConversationTask getTask() {
        return task;
    }

    public void setTask(ConversationTask task) {
        this.task = task;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }
}
