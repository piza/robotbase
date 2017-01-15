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
        if(this.taskBase.hasTaskItem("force") ){
            force="yes";
        }
        if(this.taskBase.hasTaskItem("skipPull") ){
            skipPull=true;
        }
        if(this.taskBase.hasTaskItem("skipBuild") ){
            skipBuild=true;
        }
        taskBase.sendChat("ok,start deploy task!\n pull code...");
        String pullCmd =  "pullProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if( !skipPull&& !pullCode(pullCmd)){
            taskBase.sendChat("task over");
            return;
        }

        String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if(!skipBuild  && !buildProject(buildCmd)){
            taskBase.sendChat("task over");
            return;
        }
        String deployCmd = "shell_zhiyu/deployZhiyuAdmin.sh "+ConfigUtil.getStrProp("zhiyu.projectDir")+" "+ConfigUtil.getStrProp("zhiyu.deployAdminDir");
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
