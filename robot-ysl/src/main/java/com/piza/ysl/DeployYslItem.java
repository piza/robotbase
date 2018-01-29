package com.piza.ysl;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployYslItem extends BaseItem {


    public DeployYslItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:ysl");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("ysl.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        if(this.taskBase.hasTaskItem("common") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy common");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("ysl.projectDir") + "/ysl-common";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("monitor") || this.taskBase.hasTaskItem("all") ){
            sendChat("deploy monitor");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("ysl.projectDir") + "/ysl-monitor";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_ysl/deployYsl.sh " +ConfigUtil.getStrProp("ysl.projectDir")+" "+ConfigUtil.getStrProp("ysl.yslDeployDir")+" ysl-monitor" +" ysl-monitor-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("security") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy security");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("ysl.projectDir") + "/ysl-security";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_ysl/deployYsl.sh " +ConfigUtil.getStrProp("ysl.projectDir")+" "+ConfigUtil.getStrProp("ysl.yslDeployDir")+ " ysl-security" +" ysl-security-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("admin") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy admin");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("ysl.projectDir") + "/ysl-admin";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_ysl/deployYsl.sh " +ConfigUtil.getStrProp("ysl.projectDir")+" "+ConfigUtil.getStrProp("ysl.yslDeployDir")+ " ysl-admin" +" ysl-admin-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("app") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy app");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("ysl.projectDir") + "/ysl-app";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_ysl/deployYsl.sh " +ConfigUtil.getStrProp("ysl.projectDir")+" "+ConfigUtil.getStrProp("ysl.yslDeployDir")+ " ysl-app" +" ysl-app-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("data") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy data");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("ysl.projectDir") + "/ysl-data";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_ysl/deployYsl.sh " +ConfigUtil.getStrProp("ysl.projectDir")+" "+ConfigUtil.getStrProp("ysl.yslDeployDir")+ " ysl-data" +" ysl-data-1.0-SNAPSHOT";

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
        checkShellFile(workingDir, "shell_ysl/deployYsl.sh");
        return true;
    }



}
