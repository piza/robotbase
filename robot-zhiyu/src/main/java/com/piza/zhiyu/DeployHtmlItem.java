package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

import java.io.File;

/**
 * Created by Peter on 2016/12/2.
 */
public class DeployHtmlItem extends BaseItem{

    public DeployHtmlItem(TaskBase taskBase,boolean restart) {
        super(taskBase, restart);
    }

    public DeployHtmlItem(TaskBase taskBase) {
        super(taskBase);
    }



    public void work(){
        taskBase.sendChat("start work:html");
        if(force!=null ){
            if(!checkFirst()){
                taskBase.sendChat("check file failed!");
            }else{
                taskBase.sendChat("check file success!");
            }
        }
        taskBase.sendChat("ok,start deploy task!");

        String deployCmd =  File.separator + "shell_zhiyu/deployHtml.sh "+ConfigUtil.getStrProp("zhiyu.projectDir")+" "+ConfigUtil.getStrProp("zhiyu.deployHtmlDir");
        if(!deployProject(deployCmd)){
            taskBase.sendChat("task over");
            return;
        }
        if(this.restart && !restartTomcat()){
            taskBase.sendChat("task over");
            return;
        }
    }


}
