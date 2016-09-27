package com.piza.robot.core;

import java.util.TimeZone;

/**
 * Created by Peter on 16/9/26.
 */
public class Launcher {

    public static void main(String[] args) {
        final TimeZone zone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(zone);
        ConfigUtil.initProp();
        ChatService.getInstance().start();
        System.out.println("hello world! "+Version.VERSION);
        System.out.println(ConfigUtil.getIntProp("maxPoolSize"));
        while(AppStatus.robotStatus!= AppStatus.RobotStatus.EXIT){
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
