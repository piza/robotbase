package com.piza.robot.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            StringBuilder sb = new StringBuilder();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedInputStream in = new BufferedInputStream(pid.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String s;
                        while ((s = reader.readLine()) != null) {
                            success=true;
                            sb.append(s).append("\n");
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

                            sb.append(s).append("\n");
                        }

                    }catch (IOException ioE){
                        ioE.printStackTrace();
                    }
                }
            }).start();
            pid.waitFor();
            this.result=sb.toString();
        }catch (IOException ioException){
            ioException.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
