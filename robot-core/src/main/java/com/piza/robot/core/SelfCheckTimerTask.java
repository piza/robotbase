package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * Created by Peter on 2016/10/18.
 */
public class SelfCheckTimerTask extends TimerTask {
    private static final Logger logger= Logger.getLogger(SelfCheckTimerTask.class);

    @Override
    public void run() {
        try{
            ChatService.getInstance().checkStatus();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("error when did self check:"+e.getMessage());
        }
    }
}
