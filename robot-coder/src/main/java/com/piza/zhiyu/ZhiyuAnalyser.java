package com.piza.zhiyu;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/28.
 */
public class ZhiyuAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "ZhiyuAnalyser";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && (input.startsWith("zhiyu") || input.startsWith("zy"))){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "zhiyuTask";
    }
}
