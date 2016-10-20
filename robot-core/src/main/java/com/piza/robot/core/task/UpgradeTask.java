package com.piza.robot.core.task;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.Launcher;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Created by Peter on 16/9/29.
 */
public class UpgradeTask extends TaskBase {

    private volatile static boolean working=false;
    private String force=null;

    @Override
    public String getTaskName() {
        return "upgradeTask";
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
            this.sendChat("ok,start upgrade!\n pull code...");
            if(!pullCode()){
                this.sendChat("task over");
                return;
            }
            if(!buildProject()){
                this.sendChat("task over");
                return;
            }
            if(!restartProject()){
                this.sendChat("task over");
                return;
            }
        }finally {
            working=false;
            force=null;
        }
    }

    private boolean restartProject(){

        Launcher.tryExist();

        this.sendChat("start to restart project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "restartProject.sh "+workingDir+File.separator+"robotbase";
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when restart project:" + e.getMessage());
        }

        return true;
    }
    private boolean buildProject(){
        this.sendChat("start to build project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "buildProject.sh "+workingDir+File.separator+"robotbase";
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            this.sendChat("isShellSuccess:["+shellJob.isSuccess()+"]\n"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("[ERROR]")){
                return false;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when build project:" + e.getMessage());
        }
        return false;
    }
    private boolean pullCode(){
        checkFirst();
        this.sendChat("start to pull code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "pullProject.sh "+workingDir+File.separator+"robotbase";
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

    private void checkFirst(){
        String workingDir= ConfigUtil.getStrProp("workDir");

        File robotDir=new File(workingDir);
        if(!robotDir.exists()) {
            robotDir.mkdir();
        }

        checkShellFile(workingDir,"cloneProject.sh");
        checkShellFile(workingDir,"pullProject.sh");
        checkShellFile(workingDir,"buildProject.sh");
        checkShellFile(workingDir,"restartProject.sh");
        checkShellFile(workingDir,"commitProject.sh");


        String projectDir=workingDir+ File.separator+"robotbase";

        File projectFolder=new File(projectDir);
        if(projectFolder.exists()){
            return;
        }else{
            this.sendChat("first time upgrade, clone code now!");
            //create git clone shell

            try {
                String cloneCmd = workingDir + File.separator + "cloneProject.sh "+workingDir+" git@github.com:piza/robotbase.git";
                ShellJob shellJob=new ShellJob();
                shellJob.runCommand(cloneCmd);
                this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            }catch (Exception e){
                e.printStackTrace();
                this.sendChat("error when clone project:"+e.getMessage());
                projectFolder.deleteOnExit();
            }

        }

        return;
    }


    private void checkShellFile(String workingDir,String shellName){
        File shellFile=new File(workingDir+File.separator+shellName);
        if(force!=null){
            shellFile.deleteOnExit();
        }
        if(!shellFile.exists() || force!=null) {
            try {
                this.sendChat("copy "+shellName);
                FileUtils.copyInputStreamToFile(UpgradeTask.class.getClassLoader().getResourceAsStream("shells/"+shellName), shellFile);
                shellFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
