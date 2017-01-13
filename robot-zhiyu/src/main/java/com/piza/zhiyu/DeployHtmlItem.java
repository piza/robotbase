package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Peter on 2016/12/2.
 */
public class DeployHtmlItem {

    private TaskBase taskBase;
    private String force=null;
    private boolean restart=true;

    public DeployHtmlItem(TaskBase taskBase,boolean restart) {
        this.taskBase=taskBase;
        this.restart=restart;
    }

    public DeployHtmlItem(TaskBase taskBase) {
        this.taskBase=taskBase;
    }



    public void work(){
        if(this.taskBase.hasTaskItem("force") ){
            force="yes";
            if(!checkFirst()){
                taskBase.sendChat("check file failed!");
            }else{
                taskBase.sendChat("check file success!");
            }
        }
        taskBase.sendChat("ok,start deploy task!");
        if(!deployProject()){
            taskBase.sendChat("task over");
            return;
        }
        if(this.restart && !restartTomcat()){
            taskBase.sendChat("task over");
            return;
        }
    }



    private boolean deployProject(){

        taskBase.sendChat("start to deploy project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "shell_zhiyu/deployHtml.sh "+ConfigUtil.getStrProp("zhiyu.projectDir")+" "+ConfigUtil.getStrProp("zhiyu.deployHtmlDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            taskBase.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            taskBase.sendChat("error when deploy project:" + e.getMessage());
        }

        return true;
    }
    private boolean restartTomcat(){

        taskBase.sendChat("start to restart tomcat!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String shutdownCmd = workingDir + File.separator + "shells_deployer/shutdownTomcat.sh "+ConfigUtil.getStrProp("tomcatDir");
            ShellJob shellJob1=new ShellJob();
            shellJob1.runCommand(shutdownCmd);
            taskBase.sendChat("["+shellJob1.isSuccess()+"]"+shellJob1.getResult());
//            Thread.sleep(5000);
            String startCmd = workingDir + File.separator + "shells_deployer/startTomcat.sh "+ConfigUtil.getStrProp("tomcatDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(startCmd);
            taskBase.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            taskBase.sendChat("error when restart tomcat:" + e.getMessage());
        }

        return true;
    }

    private boolean checkFirst(){
        String workingDir= ConfigUtil.getStrProp("workDir");

        File robotDir=new File(workingDir);
        if(!robotDir.exists()) {
            robotDir.mkdir();
        }

        checkShellFile(workingDir,"shell_zhiyu/deployHtml.sh");
        checkShellFile(workingDir,"shells_deployer/shutdownTomcat.sh");
        checkShellFile(workingDir,"shells_deployer/startTomcat.sh");

        String projectDir=ConfigUtil.getStrProp("zhiyu.projectDir");

        File projectFolder=new File(projectDir);
        if(projectFolder.exists()){
            return true;
        }else{
            taskBase.sendChat("no src dir, pls clone it !");
            return false;
        }
    }

    private void checkShellFile(String workingDir,String shellName){
        File shellFile=new File(workingDir+File.separator+shellName);
        if(force!=null){
            shellFile.deleteOnExit();
        }
        if(!shellFile.exists()|| force!=null) {
            try {
                taskBase.sendChat("copy "+shellName);
                FileUtils.copyInputStreamToFile(DeployHtmlItem.class.getClassLoader().getResourceAsStream(shellName), shellFile);
                shellFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}