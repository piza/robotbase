package com.piza.zjlm;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/28.
 */
public class LanMenAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "lanmengAnalyser";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && (input.toLowerCase().startsWith("lanmeng"))){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "lanmengTask";
    }
}
