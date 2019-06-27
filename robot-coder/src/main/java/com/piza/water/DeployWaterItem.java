package com.piza.water;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployWaterItem extends BaseItem {


    public DeployWaterItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:water");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("water.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        if(this.taskBase.hasTaskItem("common") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy common");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("water.projectDir") + "/water-common";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("monitor") || this.taskBase.hasTaskItem("all") ){
            sendChat("deploy monitor");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("water.projectDir") + "/water-monitor";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_bus/deployBus.sh " +ConfigUtil.getStrProp("water.projectDir")+" "+ConfigUtil.getStrProp("water.busDeployDir")+" water-monitor" +" water-monitor-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("security") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy security");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("water.projectDir") + "/water-security";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_bus/deployBus.sh " +ConfigUtil.getStrProp("water.projectDir")+" "+ConfigUtil.getStrProp("water.busDeployDir")+ " water-security" +" water-security-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("portal") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy portal");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("water.projectDir") + "/water-portal";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_bus/deployWater.sh " +ConfigUtil.getStrProp("water.projectDir")+" "+ConfigUtil.getStrProp("water.busDeployDir")+ " water-portal" +" water-portal-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("device") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy device");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("water.projectDir") + "/water-device";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_bus/deployWater.sh " +ConfigUtil.getStrProp("water.projectDir")+" "+ConfigUtil.getStrProp("water.busDeployDir")+ " water-device" +" water-device-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("data") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy data");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("water.projectDir") + "/water-data";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_water/deployWater.sh " +ConfigUtil.getStrProp("water.projectDir")+" "+ConfigUtil.getStrProp("water.busDeployDir")+ " water-data" +" water-data-1.0-SNAPSHOT";

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
        checkShellFile(workingDir, "shell_bus/deployWater.sh");
        return true;
    }



}
