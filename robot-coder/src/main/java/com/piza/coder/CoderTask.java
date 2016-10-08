package com.piza.coder;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Peter on 16/9/28.
 */
public class CoderTask extends TaskBase {

    private volatile static boolean working=false;//make sure just run one task at the sametime

    private String force=null;

    @Override
    public String getTaskName() {
        return "coderTask";
    }

    @Override
    public void run() {

        if(working){
            this.sendChat("duplicate command!");
            return;
        }
        try {
            working=true;
            if(this.chatMessage.getContent().contains("-f") || this.chatMessage.getContent().contains("force") ){
                force="yes";
            }
            this.sendChat("ok,start deploy task!\n pull code...");
            if(!pullCode()){
                this.sendChat("task over");
                return;
            }

            if(!buildProject()){
                this.sendChat("task over");
                return;
            }
            if(!deployProject()){
                this.sendChat("task over");
                return;
            }
            if(!restartTomcat()){
                this.sendChat("task over");
                return;
            }
        }finally {
            working=false;
            force=null;
        }
    }
    private boolean deployProject(){

        this.sendChat("start to deploy project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "shells_deployer/deployPocketmoney.sh "+ConfigUtil.getStrProp("pocketmoneyDir")+" "+ConfigUtil.getStrProp("deployDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when deploy project:" + e.getMessage());
        }

        return true;
    }
    private boolean restartTomcat(){

        this.sendChat("start to restart tomcat!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String shutdownCmd = workingDir + File.separator + "shells_deployer/shutdownTomcat.sh "+ConfigUtil.getStrProp("tomcatDir");
            ShellJob shellJob1=new ShellJob();
            shellJob1.runCommand(shutdownCmd);
            this.sendChat("["+shellJob1.isSuccess()+"]"+shellJob1.getResult());
//            Thread.sleep(5000);
            String startCmd = workingDir + File.separator + "shells_deployer/startTomcat.sh "+ConfigUtil.getStrProp("tomcatDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(startCmd);
            this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when restart tomcat:" + e.getMessage());
        }

        return true;
    }
    private boolean buildProject(){
        this.sendChat("start to build project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "buildProject.sh "+ConfigUtil.getStrProp("pocketmoneyDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when build project:" + e.getMessage());
        }
        return false;
    }
    private boolean pullCode(){
        if(!checkFirst()){
           return false;
        }
        this.sendChat("start to pull code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "pullProject.sh "+ConfigUtil.getStrProp("pocketmoneyDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("Already up-to-date")){
                this.sendChat("no code changed, cancel task!");
                return false;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when pull code:" + e.getMessage());
        }
        return false;
    }

    private boolean checkFirst(){
        String workingDir= ConfigUtil.getStrProp("workDir");

        File robotDir=new File(workingDir);
        if(!robotDir.exists()) {
            robotDir.mkdir();
        }

        checkShellFile(workingDir,"shells/pullProject.sh");
        checkShellFile(workingDir,"shells/buildProject.sh");
        checkShellFile(workingDir,"shells_deployer/deployPocketmoney.sh");
        checkShellFile(workingDir,"shells_deployer/shutdownTomcat.sh");
        checkShellFile(workingDir,"shells_deployer/startTomcat.sh");

        String projectDir=ConfigUtil.getStrProp("pocketmoneyDir");

        File projectFolder=new File(projectDir);
        if(projectFolder.exists()){
            return true;
        }else{
            this.sendChat("no src dir, pls clone it !");
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
                this.sendChat("copy "+shellName);
                FileUtils.copyInputStreamToFile(CoderTask.class.getClassLoader().getResourceAsStream( shellName), shellFile);
                shellFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
