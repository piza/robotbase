package com.piza.zhiyu;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

import java.io.File;

/**
 * Created by Peter on 2016/12/2.
 */
public class DeployEnglishAssistantItem extends ZhiyuBaseItem {

    public DeployEnglishAssistantItem(TaskBase taskBase, boolean restart) {
        super(taskBase, restart);
    }

    public DeployEnglishAssistantItem(TaskBase taskBase) {
        super(taskBase);
    }



    public void work(){
        sendChat("start work:english assistatn");
        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }
        sendChat("ok,start deploy task!");

        String deployCmd =  File.separator + "shell_zhiyu/deployEnglishAssistant.sh "+ConfigUtil.getStrProp("zhiyu.englishAssistantCodeDir")+" "+ConfigUtil.getStrProp("zhiyu.englishAssistantDeployDir");
        if(!deployProject(deployCmd)){
            sendChat("task over");
            return;
        }
        
    }

    public boolean checkFirst(){
        if(!super.checkFirst()){
            return false;
        }
        String workingDir= ConfigUtil.getStrProp("workDir");
        checkShellFile(workingDir,"shell_zhiyu/deployEnglishAssistant.sh");
        return true;
    }


}
