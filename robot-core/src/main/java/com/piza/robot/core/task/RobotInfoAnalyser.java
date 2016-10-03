package com.piza.robot.core.task;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/29.
 */
public class RobotInfoAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "RobotInfoAnalyser";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && (input.startsWith("hello")
                || input.toLowerCase().equals("hi")
                || input.startsWith("hi "))){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "robotInfoTask";
    }
}
