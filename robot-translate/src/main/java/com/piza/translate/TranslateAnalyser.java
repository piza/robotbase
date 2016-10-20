package com.piza.translate;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 2016/10/20.
 */
public class TranslateAnalyser implements IAnalyser {


    @Override
    public String getName() {
        return "TranslateAnalyser";

    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && (input.startsWith("translate") || input.startsWith("trans"))){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "translateTask";
    }
}
