package com.piza.robot.core;

/**
 * Created by Peter on 16/9/28.
 */
public class ParseResult {

    protected boolean handleable;
    protected boolean continuable;

    public boolean isHandleable() {
        return handleable;
    }
    public void setHandleable(boolean handleable) {
        this.handleable = handleable;
    }
    public boolean isContinuable() {
        return continuable;
    }
    public void setContinuable(boolean continuable) {
        this.continuable = continuable;
    }
}
