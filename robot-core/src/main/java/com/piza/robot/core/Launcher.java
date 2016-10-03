package com.piza.robot.core;

import com.piza.robot.core.task.*;

import java.util.TimeZone;

/**
 * Created by Peter on 16/9/26.
 */
public class Launcher {

    public void init(){
        final TimeZone zone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(zone);
        ConfigUtil.initProp("config.properties");
        //add basic task
        ParserManage.getInstance().addAnalyser(new RobotInfoAnalyser());
        TaskManager.getInstance().addTask(new RobotInfoTask());
        TaskManager.getInstance().addTask(new DefaultTask());

        ParserManage.getInstance().addAnalyser(new UpgradeAnalyser());
        TaskManager.getInstance().addTask(new UpgradeTask());


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

    public static void tryExist(){
        System.out.println(Thread.currentThread().getName()+" notify all components that system will shutdown in 5s!");
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
