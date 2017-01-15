package com.piza.zhiyu;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Peter on 2017/1/15.
 */
public abstract class BaseItem {

    protected TaskBase taskBase;
    protected String force=null;
    protected boolean skipPull=false;
    protected boolean skipBuild=false;
    protected boolean restart=true;


    public BaseItem(TaskBase taskBase) {
        this.taskBase=taskBase;
    }

    public BaseItem(TaskBase taskBase,boolean restart) {
        this.taskBase=taskBase;
        this.restart=restart;
    }

    protected boolean deployProject(String deployCmd){

        taskBase.sendChat("start to deploy project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(workingDir+File.separator +deployCmd);
            taskBase.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            taskBase.sendChat("error when deploy project:" + e.getMessage());
        }

        return true;
    }
    protected boolean restartTomcat(){

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

    protected boolean buildProject(String buildCmd){
        taskBase.sendChat("start to build project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(workingDir+File.separator +buildCmd);
            taskBase.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("[ERROR]")){
                taskBase.sendChat("build failed, break task!");
                return false;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            taskBase.sendChat("error when build project:" + e.getMessage());
        }
        return false;
    }
    protected boolean pullCode(String pullCmd){
        if(!checkFirst()){
            return false;
        }
        taskBase.sendChat("start to pull code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(workingDir + File.separator +pullCmd);
            taskBase.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("Already up-to-date")){
                taskBase.sendChat("no code changed, cancel task!");
                return false;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            taskBase.sendChat("error when pull code:" + e.getMessage());
        }
        return false;
    }

    protected boolean checkFirst(){
        String workingDir= ConfigUtil.getStrProp("workDir");

        File robotDir=new File(workingDir);
        if(!robotDir.exists()) {
            robotDir.mkdir();
        }

        checkShellFile(workingDir,"shells/pullProject.sh");
        checkShellFile(workingDir,"shells/buildProject.sh");
        checkShellFile(workingDir,"shell_zhiyu/deployZhiyuAdmin.sh");
        checkShellFile(workingDir,"shells_deployer/shutdownTomcat.sh");
        checkShellFile(workingDir,"shells_deployer/startTomcat.sh");
        checkShellFile(workingDir,"shell_zhiyu/deployHtml.sh");
        checkShellFile(workingDir, "shell_zhiyu/deployPocketmoney.sh");
        checkShellFile(workingDir,"shell_zhiyu/deployZhiyu.sh");

        String projectDir=ConfigUtil.getStrProp("zhiyu.projectDir");

        File projectFolder=new File(projectDir);
        if(projectFolder.exists()){
            return true;
        }else{
            taskBase.sendChat("no src dir, pls clone it !");
            return false;
        }
    }


    protected void checkShellFile(String workingDir,String shellName){
        File shellFile=new File(workingDir+File.separator+shellName);
        if(force!=null){
            shellFile.deleteOnExit();
        }
        if(!shellFile.exists()|| force!=null) {
            try {
                taskBase.sendChat("copy "+shellName);
                FileUtils.copyInputStreamToFile(DeployAdminItem.class.getClassLoader().getResourceAsStream(shellName), shellFile);
                shellFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
