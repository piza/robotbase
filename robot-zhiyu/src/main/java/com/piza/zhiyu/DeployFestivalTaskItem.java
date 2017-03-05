package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/** deploy festival
 * Created by Peter on 2016/12/2.
 */
public class DeployFestivalTaskItem extends BaseItem{


    private boolean justDeploy;

    public DeployFestivalTaskItem(TaskBase taskBase) {
        super(taskBase);
    }
    public DeployFestivalTaskItem(TaskBase taskBase, boolean justDeploy) {
        super(taskBase);
        this.justDeploy=justDeploy;
    }


    public void work(){
        sendChat("start work:festival");
        if (!this.justDeploy) {
            sendChat("ok,start deploy task!\n pull code...");
            String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
            if (!skipPull && !pullCode(pullCmd)) {
                sendChat("task over");
                return;
            }

            String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
            if (!skipBuild && !buildProject(buildCmd)) {
                sendChat("task over");
                return;
            }
        }
        String deployCmd = "shell_zhiyu/deployZhiyu.sh "+ConfigUtil.getStrProp("zhiyu.projectDir")+" "+ConfigUtil.getStrProp("zhiyu.deployFestivalDir");
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
