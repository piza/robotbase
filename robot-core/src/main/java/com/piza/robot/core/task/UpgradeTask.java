package com.piza.robot.core.task;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Created by Peter on 16/9/29.
 */
public class UpgradeTask extends TaskBase {
    @Override
    public String getTaskName() {
        return "upgradeTask";
    }

    @Override
    public void run() {
      this.sendChat("ok,start upgrade!\n pull code...");
        pullCode();
    }

    private void pullCode(){
        checkFirst();
        this.sendChat("start to pull code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "pullProject.sh "+workingDir+File.separator+"robotbase";
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            this.sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
        }catch (Exception e){
            e.printStackTrace();
            this.sendChat("error when clone project:" + e.getMessage());
        }
    }

    private void checkFirst(){
        String workingDir= ConfigUtil.getStrProp("workDir");

        File robotDir=new File(workingDir);
        if(!robotDir.exists()) {
            robotDir.mkdir();
        }

        String projectDir=workingDir+ File.separator+"robotbase";

        File projectFolder=new File(projectDir);
        if(projectFolder.exists()){
            return;
        }else{
            this.sendChat("first time upgrade, clone code now!");
            //create git clone shell
            File cloneShell=new File(workingDir+File.separator+"cloneProject.sh");
            File pullShell=new File(workingDir+File.separator+"pullProject.sh");

            if(!cloneShell.exists()){
                try {
                    this.sendChat("copy cloneProject.sh & pullProject.sh");
                    FileUtils.copyInputStreamToFile(UpgradeTask.class.getClassLoader().getResourceAsStream("shells/cloneProject.sh"),cloneShell);
                    cloneShell.setExecutable(true);

                    FileUtils.copyInputStreamToFile(UpgradeTask.class.getClassLoader().getResourceAsStream("shells/pullProject.sh"),pullShell);
                    pullShell.setExecutable(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
}
