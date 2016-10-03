package com.piza.robot.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Peter on 2016/10/3.
 */
public class ShellJob {

    private boolean success=false;

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void runCommand(String command){
        try {
//            String[] cmd = {"/bin/bash", "-c", command};

            ProcessBuilder pb=new ProcessBuilder("/bin/bash","-c",command);
//            Process pid = Runtime.getRuntime().exec(cmd);
            pb.redirectErrorStream(true);
            Process pid=pb.start();
            List<String> jobInfo= Collections.synchronizedList(new ArrayList());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedInputStream in = new BufferedInputStream(pid.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String s;
                        while ((s = reader.readLine()) != null) {
                            success=true;
                            jobInfo.add(s+"\n");
                        }
                    }catch (IOException ioE){
                        ioE.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedInputStream errIn = new BufferedInputStream(pid.getErrorStream());

                        BufferedReader errReader = new BufferedReader(new InputStreamReader(errIn));
                        String s;

                        while ((s = errReader.readLine()) != null) {
                            jobInfo.add(s+"\n");
                        }

                    }catch (IOException ioE){
                        ioE.printStackTrace();
                    }
                }
            }).start();
            pid.waitFor();
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<jobInfo.size();i++){
                sb.append(jobInfo.get(i));
                if(i>5 && jobInfo.size()>25 && i<jobInfo.size()-15){
                    i=jobInfo.size()-15;
                }
            }

            this.result=sb.toString();
        }catch (IOException ioException){
            ioException.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
