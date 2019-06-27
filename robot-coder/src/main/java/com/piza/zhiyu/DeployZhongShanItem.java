package com.piza.zhiyu;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployZhongShanItem extends BaseItem {


    public DeployZhongShanItem(TaskBase taskBase, boolean restart) {
        super(taskBase,restart);
    }

    public void work() {
        sendChat("start work:zhongshan");

        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }

        sendChat("ok,start deploy task!\n pull code...");
        String pullCmd = "pullProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if(!skipPull&&!pullCode(pullCmd)){
            sendChat("task over");
            return;
        }

        String buildCmd =  "buildProject.sh "+ConfigUtil.getStrProp("zhiyu.projectDir");
        if(!skipBuild  && !buildProject(buildCmd)){
            sendChat("task over");
            return;
        }
        String deployCmd = "shell_zhiyu/deployZhongShan.sh "+ConfigUtil.getStrProp("zhiyu.zhongshanDir")+" "+ConfigUtil.getStrProp("zhiyu.zhongshanDeployDir");

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
        checkShellFile(workingDir,"shell_zhiyu/deployZhongShan.sh");
        return true;
    }

}
