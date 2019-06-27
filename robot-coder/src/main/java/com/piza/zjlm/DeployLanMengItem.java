package com.piza.zjlm;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployLanMengItem extends BaseItem {


    public DeployLanMengItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:lanmeng");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        if(this.taskBase.hasTaskItem("common") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy common");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir") + "/lanmeng-common";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("monitor") || this.taskBase.hasTaskItem("all") ){
            sendChat("deploy monitor");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir") + "/lanmeng-monitor";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_lanmeng/deployLanmeng.sh " +ConfigUtil.getStrProp("lanmeng.projectDir")+" "+ConfigUtil.getStrProp("lanmeng.lanmengDeployDir")+" lanmeng-monitor" +" lanmeng-monitor-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("security") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy security");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir") + "/lanmeng-security";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_lanmeng/deployLanmeng.sh " +ConfigUtil.getStrProp("lanmeng.projectDir")+" "+ConfigUtil.getStrProp("lanmeng.lanmengDeployDir")+ " lanmeng-security" +" lanmeng-security-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("portal") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy portal");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir") + "/lanmeng-portal";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_lanmeng/deployLanmeng.sh " +ConfigUtil.getStrProp("lanmeng.projectDir")+" "+ConfigUtil.getStrProp("lanmeng.lanmengDeployDir")+ " lanmeng-portal" +" lanmeng-portal-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("device") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy device");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir") + "/lanmeng-device";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_lanmeng/deployLanmeng.sh " +ConfigUtil.getStrProp("lanmeng.projectDir")+" "+ConfigUtil.getStrProp("lanmeng.lanmengDeployDir")+ " lanmeng-device" +" lanmeng-device-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("data") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy data");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("lanmeng.projectDir") + "/lanmeng-data";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_lanmeng/deployLanmeng.sh " +ConfigUtil.getStrProp("lanmeng.projectDir")+" "+ConfigUtil.getStrProp("lanmeng.lanmengDeployDir")+ " lanmeng-data" +" lanmeng-data-1.0-SNAPSHOT";

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
        checkShellFile(workingDir, "shell_lanmeng/deployLanmeng.sh");
        return true;
    }



}
