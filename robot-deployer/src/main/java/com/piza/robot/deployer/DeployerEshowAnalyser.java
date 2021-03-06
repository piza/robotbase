package com.piza.robot.deployer;

import com.piza.robot.core.Analysis;
import com.piza.robot.core.IAnalyser;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployerEshowAnalyser implements IAnalyser {
    @Override
    public String getName() {
        return "DeployerEshowAnalyser";
    }

    @Override
    public Analysis analyse(String input) {
        Analysis analysis=new Analysis();
        if(input!=null && input.startsWith("eshow")){
            analysis.setHandleable(true);
        }
        return analysis;
    }

    @Override
    public String getTaskName() {
        return "eshowTask";
    }
}
