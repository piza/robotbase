package com.piza.festival;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Peter on 2016/12/2.
 */
public class DeployTaskItem {

    private TaskBase taskBase;
    private String force=null;

    public DeployTaskItem(TaskBase taskBase) {
        this.taskBase=taskBase;
    }


    public void work(){
        if(this.taskBase.hasTaskItem("force") ){
            force="yes";
        }
        taskBase.sendChat("ok,start deploy task!\n pull code...");
        if(!pullCode()){
            taskBase.sendChat("task over");
            return;
        }

        if(!buildProject()){
            taskBase.sendChat("task over");
            return;
        }
        if(!deployProject()){
            taskBase.sendChat("task over");
            return;
        }
        if(!restartTomcat()){
            taskBase.sendChat("task over");
            return;
        }
    }



    private boolean deployProject(){

        taskBase.sendChat("start to deploy project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "shells_deployer/deployFestival.sh "+ConfigUtil.getStrProp("festival.projectDir")+" "+ConfigUtil.getStrProp("festival.deployDir");
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
    private boolean buildProject(){
        taskBase.sendChat("start to build project!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "buildProject.sh "+ConfigUtil.getStrProp("festival.projectDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
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
    private boolean pullCode(){
        if(!checkFirst()){
            return false;
        }
        taskBase.sendChat("start to pull code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "pullProject.sh "+ConfigUtil.getStrProp("festival.projectDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
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

    private boolean checkFirst(){
        String workingDir= ConfigUtil.getStrProp("workDir");

        File robotDir=new File(workingDir);
        if(!robotDir.exists()) {
            robotDir.mkdir();
        }

        checkShellFile(workingDir,"shells/pullProject.sh");
        checkShellFile(workingDir,"shells/buildProject.sh");
        checkShellFile(workingDir,"shell_festival/deployFestival.sh");
        checkShellFile(workingDir,"shells_deployer/shutdownTomcat.sh");
        checkShellFile(workingDir,"shells_deployer/startTomcat.sh");

        String projectDir=ConfigUtil.getStrProp("festival.projectDir");

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
                FileUtils.copyInputStreamToFile(DeployTaskItem.class.getClassLoader().getResourceAsStream(shellName), shellFile);
                shellFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
