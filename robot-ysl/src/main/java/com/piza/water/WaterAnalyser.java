package com.piza.water;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/28.
 */
public class WaterAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "waterAnalyserv";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && (input.toLowerCase().startsWith("water"))){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "waterTask";
    }
}
