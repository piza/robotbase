package com.piza.robot.core;

/**
 * Created by Peter on 2016/10/21.
 */
public class WebResponse {


    protected Integer errorType;

    protected String errorMesg;


    public Integer getErrorType() {
        return errorType;
    }

    public void setErrorType(Integer errorType) {
        this.errorType = errorType;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }
}
