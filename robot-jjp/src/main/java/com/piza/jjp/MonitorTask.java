package com.piza.jjp;

import com.piza.robot.core.ChatService;
import com.piza.robot.core.SelfCheckTimerTask;
import org.apache.log4j.Logger;

import java.util.TimerTask;

public class MonitorTask extends TimerTask {
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
