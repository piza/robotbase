package com.piza.cpc;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployCpcItem extends BaseItem {


    public DeployCpcItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:cpc");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("cpc.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        if(this.taskBase.hasTaskItem("common") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy common");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("cpc.projectDir") + "/cpc-common";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("monitor") || this.taskBase.hasTaskItem("all") ){
            sendChat("deploy monitor");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("cpc.projectDir") + "/cpc-monitor";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_cpc/deployCpc.sh " +ConfigUtil.getStrProp("cpc.projectDir")+" "+ConfigUtil.getStrProp("cpc.cpcDeployDir")+" cpc-monitor" +" cpc-monitor-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("security") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy security");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("cpc.projectDir") + "/cpc-security";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_cpc/deployCpc.sh " +ConfigUtil.getStrProp("cpc.projectDir")+" "+ConfigUtil.getStrProp("cpc.cpcDeployDir")+ " cpc-security" +" cpc-security-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("portal") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy portal");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("cpc.projectDir") + "/cpc-portal";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_cpc/deployCpc.sh " +ConfigUtil.getStrProp("cpc.projectDir")+" "+ConfigUtil.getStrProp("cpc.cpcDeployDir")+ " cpc-portal" +" cpc-portal-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("device") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy device");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("cpc.projectDir") + "/cpc-device";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_cpc/deployCpc.sh " +ConfigUtil.getStrProp("cpc.projectDir")+" "+ConfigUtil.getStrProp("cpc.cpcDeployDir")+ " cpc-device" +" cpc-device-1.0-SNAPSHOT";

            if(!deployProject(deployCmd)){
                sendChat("task over");
                return;
            }
        }

        if(this.taskBase.hasTaskItem("data") || this.taskBase.hasTaskItem("all")){
            sendChat("deploy data");
            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("cpc.projectDir") + "/cpc-data";
            if(!skipBuild  && !buildProject(buildCmd)){
                sendChat("task over");
                return;
            }
            String deployCmd = "shell_cpc/deployCpc.sh " +ConfigUtil.getStrProp("cpc.projectDir")+" "+ConfigUtil.getStrProp("cpc.cpcDeployDir")+ " cpc-data" +" cpc-data-1.0-SNAPSHOT";

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
        checkShellFile(workingDir, "shell_cpc/deployCpc.sh");
        return true;
    }



}
