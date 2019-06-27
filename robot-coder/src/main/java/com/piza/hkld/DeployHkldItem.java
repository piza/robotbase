package com.piza.hkld;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployHkldItem extends BaseItem {


    public DeployHkldItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:hkld");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("hkld.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        if(this.taskBase.hasTaskItem("common") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy common");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("hkld.projectDir") + "/hkld-common";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("monitor") || this.taskBase.hasTaskItem("all") ){
            sendChat("deploy monitor");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("hkld.projectDir") + "/hkld-monitor";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_hkld/deployHkld.sh " +ConfigUtil.getStrProp("hkld.projectDir")+" "+ConfigUtil.getStrProp("hkld.hkldDeployDir")+" hkld-monitor" +" hkld-monitor-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("security") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy security");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("hkld.projectDir") + "/hkld-security";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_hkld/deployHkld.sh " +ConfigUtil.getStrProp("hkld.projectDir")+" "+ConfigUtil.getStrProp("hkld.hkldDeployDir")+ " hkld-security" +" hkld-security-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("portal") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy portal");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("hkld.projectDir") + "/hkld-portal";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_hkld/deployHkld.sh " +ConfigUtil.getStrProp("hkld.projectDir")+" "+ConfigUtil.getStrProp("hkld.hkldDeployDir")+ " hkld-portal" +" hkld-portal-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("device") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy device");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("hkld.projectDir") + "/hkld-device";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_hkld/deployHkld.sh " +ConfigUtil.getStrProp("hkld.projectDir")+" "+ConfigUtil.getStrProp("hkld.hkldDeployDir")+ " hkld-device" +" hkld-device-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("data") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy data");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("hkld.projectDir") + "/hkld-data";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_hkld/deployHkld.sh " +ConfigUtil.getStrProp("hkld.projectDir")+" "+ConfigUtil.getStrProp("hkld.hkldDeployDir")+ " hkld-data" +" hkld-data-1.0-SNAPSHOT";

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
        checkShellFile(workingDir, "shell_hkld/deployHkld.sh");
        return true;
    }



}
