package com.piza.zhiyu;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

import java.io.File;

/**
 * Created by Peter on 2016/12/2.
 */
public class DeployHtmlItem extends BaseItem {

    public DeployHtmlItem(TaskBase taskBase,boolean restart) {
        super(taskBase, restart);
    }

    public DeployHtmlItem(TaskBase taskBase) {
        super(taskBase);
    }



    public void work(){
        sendChat("start work:html");
        if(force!=null ){
            if(!checkFirst()){
                sendChat("check file failed!");
            }else{
                sendChat("check file success!");
            }
        }
        sendChat("ok,start deploy task!");

        String deployCmd =  File.separator + "shell_zhiyu/deployHtml.sh "+ConfigUtil.getStrProp("zhiyu.projectDir")+" "+ConfigUtil.getStrProp("zhiyu.deployHtmlDir");
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
