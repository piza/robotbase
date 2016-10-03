package com.piza.robot.core.task;

import com.piza.robot.core.ConfigUtil;
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
    }

    private void pullCode(){
        checkFirst();
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "pullProject.sh "+workingDir+File.separator+"robotbase";
            String[] cmd = {"/bin/bash", "-c", pullCmd};

            Process pid = Runtime.getRuntime().exec(cmd);
            BufferedInputStream in = new BufferedInputStream(pid.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String s;
            StringBuilder sb=new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s+"\n");
            }
            pid.waitFor();
            this.sendChat(sb.toString());
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
                String cloneCmd = workingDir + File.separator + "cloneProject.sh git@github.com:piza/robotbase.git";
                String[] cmd = {"/bin/bash", "-c", cloneCmd};

                Process pid = Runtime.getRuntime().exec(cmd);
                BufferedInputStream in = new BufferedInputStream(pid.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String s;
                while ((s = reader.readLine()) != null) {
                    if (s.toLowerCase().indexOf("checking connectivity... done.") > -1) {
                        this.sendChat("clone project done!");
                        return ;
                    }
                }
                pid.waitFor();
            }catch (Exception e){
                e.printStackTrace();
                this.sendChat("error when clone project:"+e.getMessage());
                projectFolder.deleteOnExit();
            }

        }

        return;
    }
}
