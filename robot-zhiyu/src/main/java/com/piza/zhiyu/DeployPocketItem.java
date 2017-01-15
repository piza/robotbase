package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployPocketItem extends BaseItem{

    private volatile static boolean working=false;//make sure just run one task at the sametime

    public DeployPocketItem(TaskBase taskBase,boolean restart) {
        super(taskBase,restart);
    }

    public void work() {

        if(working){
            taskBase.sendChat("duplicate command!");
            return;
        }
        try {
            working=true;
            if(taskBase.hasTaskItem("-f") || taskBase.hasTaskItem("force") ){
                force="yes";
            }
            taskBase.sendChat("ok,start deploy task!\n pull code...");
            String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("pocketmoneyDir");
            if(!pullCode(pullCmd)){
                taskBase.sendChat("task over");
                return;
            }

            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("pocketmoneyDir");
            if(!buildProject(buildCmd)){
                taskBase.sendChat("task over");
                return;
            }
            String deployCmd = "shells_deployer/deployPocketmoney.sh "+ConfigUtil.getStrProp("pocketmoneyDir")+" "+ConfigUtil.getStrProp("deployDir");

            if(!deployProject(deployCmd)){
                taskBase.sendChat("task over");
                return;
            }
            if(!restartTomcat()){
                taskBase.sendChat("task over");
                return;
            }
        }finally {
            working=false;
            force=null;
        }
    }

}
