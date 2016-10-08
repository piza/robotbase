package com.piza.coder;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/28.
 */
public class CoderAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "CoderAnalyser";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && input.startsWith("code")){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "coderTask";
    }
}
