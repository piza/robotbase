package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployPocketItem extends BaseItem{


    public DeployPocketItem(TaskBase taskBase,boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        taskBase.sendChat("start work:pocket");

        taskBase.sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("pocketmoneyDir");
        if(!skipPull&&!pullCode(pullCmd)){
            taskBase.sendChat("task over");
            return;
        }

        String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("pocketmoneyDir");
        if(!skipBuild  && !buildProject(buildCmd)){
            taskBase.sendChat("task over");
            return;
        }
        String deployCmd = "shells_deployer/deployPocketmoney.sh "+ConfigUtil.getStrProp("pocketmoneyDir")+" "+ConfigUtil.getStrProp("deployDir");

        if(!deployProject(deployCmd)){
            taskBase.sendChat("task over");
            return;
        }
        if(this.restart && !restartTomcat()){
            taskBase.sendChat("task over");
            return;
        }
    }

}
