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

        if(this.taskBase.hasTaskItem("common") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy common");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("jjp.projectDir") + "/jjp-common";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("monitor") || this.taskBase.hasTaskItem("all") ){
            sendChat("deploy monitor");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("jjp.projectDir") + "/jjp-monitor";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_jjp/deployJjp.sh "+ConfigUtil.getStrProp("jjp.projectDir")+" "+ConfigUtil.getStrProp("jjp.jjpDeployDir")+" jjp-monitor" +" jjp-monitor-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("security") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy security");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("jjp.projectDir") + "/jjp-security";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_jjp/deployJjp.sh "+ConfigUtil.getStrProp("jjp.projectDir")+" "+ConfigUtil.getStrProp("jjp.jjpDeployDir")+ " jjp-security" +" jjp-security-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("admin") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy admin");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("jjp.projectDir") + "/jjp-admin";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_jjp/deployJjp.sh "+ConfigUtil.getStrProp("jjp.projectDir")+" "+ConfigUtil.getStrProp("jjp.jjpDeployDir")+ " jjp-admin" +" jjp-admin-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        sendChat("task over");
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
