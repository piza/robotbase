package com.piza.robot.core;

import com.piza.robot.core.task.DefaultTask;
import com.piza.robot.core.task.RobotInfoAnalyser;
import com.piza.robot.core.task.RobotInfoTask;

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
        ParserManage.getInstance().addAnalyser("robotInfo", new RobotInfoAnalyser());
        TaskManager.getInstance().addTask(new RobotInfoTask());
        TaskManager.getInstance().addTask(new DefaultTask());

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
