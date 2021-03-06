package com.piza.robot.core;

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
        init();
    }

    public BaseItem(TaskBase taskBase,boolean restart) {
        this.taskBase=taskBase;
        this.restart=restart;
        init();
    }

    protected void init(){
        if(this.taskBase.hasTaskItem("force") ){
            force="yes";
        }
        if(this.taskBase.hasTaskItem("skipPull") ){
            skipPull=true;
        }
        if(this.taskBase.hasTaskItem("skipBuild") ){
            skipBuild=true;
        }
    }

    protected boolean deployProject(String deployCmd){

        sendChat("start to deploy project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(workingDir+File.separator +deployCmd);
            sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            sendChat("error when deploy project:" + e.getMessage());
        }

        return true;
    }
    protected boolean restartTomcat(){

        sendChat("start to restart tomcat!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String shutdownCmd = workingDir + File.separator + "shells_deployer/shutdownTomcat.sh "+ConfigUtil.getStrProp("tomcatDir");
            ShellJob shellJob1=new ShellJob();
            shellJob1.runCommand(shutdownCmd);
            sendChat("["+shellJob1.isSuccess()+"]"+shellJob1.getResult());
//            Thread.sleep(5000);
            String startCmd = workingDir + File.separator + "shells_deployer/startTomcat.sh "+ConfigUtil.getStrProp("tomcatDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(startCmd);
            sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            sendChat("error when restart tomcat:" + e.getMessage());
        }

        return true;
    }

    protected boolean buildProject(String buildCmd){
        sendChat("start to build project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(workingDir+File.separator +buildCmd);
            sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("[ERROR]")){
                sendChat("build failed, break task!");
                return false;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            sendChat("error when build project:" + e.getMessage());
        }
        return false;
    }
    protected boolean pullCode(String pullCmd){
        if(!checkFirst()){
            return false;
        }
        sendChat("start to pull code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(workingDir + File.separator +pullCmd);
            sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("Already up-to-date")){
                sendChat("no code changed, cancel task!");
                return false;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            sendChat("error when pull code:" + e.getMessage());
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

        checkShellFile(workingDir,"shells_deployer/shutdownTomcat.sh");
        checkShellFile(workingDir,"shells_deployer/startTomcat.sh");
        return true;
    }


    protected void checkShellFile(String workingDir,String shellName){
        File shellFile=new File(workingDir+File.separator+shellName);
        if(force!=null){
            shellFile.deleteOnExit();
        }
        if(!shellFile.exists()|| force!=null) {
            try {
                sendChat("copy "+shellName);
                FileUtils.copyInputStreamToFile(BaseItem.class.getClassLoader().getResourceAsStream(shellName), shellFile);
                shellFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendChat(String msg){
        taskBase.sendChat(msg);
    }
}
