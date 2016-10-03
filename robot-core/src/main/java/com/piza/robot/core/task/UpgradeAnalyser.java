package com.piza.robot.core.task;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/29.
 */
public class UpgradeAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "UpgradeAnalyser";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && (input.startsWith("upgrade")
                || input.toLowerCase().equals("升级")
                )){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "upgradeTask";
    }
}
