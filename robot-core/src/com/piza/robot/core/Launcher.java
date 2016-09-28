package com.piza.robot.core;

import java.util.TimeZone;

/**
 * Created by Peter on 16/9/26.
 */
public class Launcher {

    public void init(){
        final TimeZone zone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(zone);
        ConfigUtil.initProp();


    }

    public void startApp(){
        ChatService.getInstance().start();

        while(AppStatus.robotStatus!= AppStatus.RobotStatus.EXIT){
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
