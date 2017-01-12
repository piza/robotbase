package com.piza.zhiyu;

import com.piza.robot.core.TaskBase;
import org.apache.log4j.Logger;

/**
 * Created by Peter on 16/9/28.
 */
public class ZhiyuTask extends TaskBase {

    private static final Logger logger= Logger.getLogger(ZhiyuTask.class);

    private volatile static boolean working=false;//make sure just run one task at the sametime

    @Override
    public String getTaskName() {
        return "zhiyuTask";
    }

    @Override
    public void run() {

        if(working){
            this.sendChat("duplicate command!");
            return;
        }
        try {
            working=true;
            if(this.hasTaskItem("code")){
                CodeTaskItem codeTaskItem=new CodeTaskItem(this);
                codeTaskItem.work();
            }else if(this.hasTaskItem("deployAll")){
                (new DeployHtmlItem(this,false)).work();
                (new DeployTaskItem(this)).work();
            }else if(this.hasTaskItem("deploy")){
                (new DeployTaskItem(this)).work();
            }else if(this.hasTaskItem("html")){
                (new DeployHtmlItem(this)).work();
            }else if(this.hasTaskItem("admin")){
                (new DeployAdminItem(this)).work();
            }else if(this.hasTaskItem("java")){
                (new DeployAdminItem(this,false)).work();
                (new DeployTaskItem(this,true)).work();
            }else{
                this.sendChat("help:\n code       generate code\n deploy    deploy project" +
                        "\n html    deploy wechat html project" +
                        "\n admin    deploy admin project" +
                        "\n skipBuild    skip build" +
                        "\n skipPull     skip pull code "+
                        "\n deployAll     deploy admin & portal "
                        );
            }

        }finally {
            working=false;
        }
    }



}
