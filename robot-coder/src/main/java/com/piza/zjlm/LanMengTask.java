package com.piza.zjlm;

import com.piza.robot.core.TaskBase;
import org.apache.log4j.Logger;

/**
 * Created by Peter on 16/9/28.
 */
public class LanMengTask extends TaskBase {

    private static final Logger logger= Logger.getLogger(LanMengTask.class);

    private volatile static boolean working=false;//make sure just run one task at the sametime

    @Override
    public String getTaskName() {
        return "lanmengTask";
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
                LanMengOrmTaskItem codeTaskItem=new LanMengOrmTaskItem(this);
                codeTaskItem.work();
            }else if(this.hasTaskItem("java")){
                (new DeployLanMengItem(this,false)).work();
            }else{
                this.sendChat("help:\n code       generate code\n deploy    deploy project" +
                        "\n html    deploy wechat html project" +
                        "\n java    deploy java project" +
                        "\n skipBuild    skip build" +
                        "\n skipPull     skip pull code "+
                        "\n deployAll     deploy admin & app "
                        );
            }

        }finally {
            working=false;
        }
    }



}
