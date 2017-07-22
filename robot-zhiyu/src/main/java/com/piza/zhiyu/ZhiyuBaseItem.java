package com.piza.zhiyu;

import com.piza.robot.core.BaseItem;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;

import java.io.File;

/**
 * Created by Peter on 2017/7/22.
 */
public class ZhiyuBaseItem extends BaseItem {


    public ZhiyuBaseItem(TaskBase taskBase,boolean restart) {
        super(taskBase,restart);
    }

    public ZhiyuBaseItem(TaskBase taskBase) {
        super(taskBase);
    }

    @Override
    protected boolean checkFirst() {

        boolean res=super.checkFirst();
        String workingDir= ConfigUtil.getStrProp("workDir");

        checkShellFile(workingDir,"shell_zhiyu/deployZhiyuAdmin.sh");
        checkShellFile(workingDir,"shell_zhiyu/deployHtml.sh");
        checkShellFile(workingDir, "shell_zhiyu/deployPocketmoney.sh");
        checkShellFile(workingDir,"shell_zhiyu/deployZhiyu.sh");

        String projectDir= ConfigUtil.getStrProp("zhiyu.projectDir");

        File projectFolder=new File(projectDir);
        if(projectFolder.exists()){
            return res;
        }else{
            sendChat("ZhiyuBaseItem: no src dir, pls clone it !");
            return false;
        }
    }
}
