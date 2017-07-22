package com.piza.jjp;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployJJPItem extends BaseItem {


    public DeployJJPItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:jjp");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("jjp.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("jjp.projectDir");
        if(!skipBuild  && !buildProject(buildCmd)){
            sendChat("task over");
            return;
        }
        String deployCmd = "shell_jjp/deployJjp.sh "+ConfigUtil.getStrProp("jjp.jjpDir")+" "+ConfigUtil.getStrProp("jjp.jjpDeployDir");

        if(!deployProject(deployCmd)){
            sendChat("task over");
            return;
        }
        if(this.restart && !restartTomcat()){
            sendChat("task over");
            return;
        }
    }

    public boolean checkFirst(){
        if(!super.checkFirst()){
            return false;
        }
        String workingDir= ConfigUtil.getStrProp("workDir");
        checkShellFile(workingDir,"shell_jjp/deployJjp.sh");
        return true;
    }

}
