package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

import java.io.File;

/**
 * Created by Peter on 2016/12/2.
 */
public class DeployAdminItem extends BaseItem{




    public DeployAdminItem(TaskBase taskBase) {
        super(taskBase);
    }

    public DeployAdminItem(TaskBase taskBase,boolean restart) {
        super(taskBase,restart);
    }

    public void work(){
        sendChat("start work:admin");
        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd =  "pullProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if( !skipPull&& !pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if(!skipBuild  && !buildProject(buildCmd)){
            sendChat("task over");
            return;
        }
        String deployCmd = "shell_zhiyu/deployZhiyuAdmin.sh "+ConfigUtil.getStrProp("zhiyu.projectDir")+" "+ConfigUtil.getStrProp("zhiyu.deployAdminDir");
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
