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
        sendChat("start work:pocket");

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if(!skipBuild  && !buildProject(buildCmd)){
            sendChat("task over");
            return;
        }
        String deployCmd = "shell_zhiyu/deployPocketmoney.sh "+ConfigUtil.getStrProp("zhiyu.pocketDir")+" "+ConfigUtil.getStrProp("zhiyu.pocketDeployDir");

        if(!deployProject(deployCmd)){
            sendChat("task over");
            return;
        }
        if(this.restart && !restartTomcat()){
            sendChat("task over");
            return;
        }
    }

}
