package com.piza.robot.deployer;

import com.piza.robot.core.Launcher;
import com.piza.robot.core.ParserManage;
import com.piza.robot.core.TaskManager;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployerLauncher extends Launcher {



    public static void main(String[] args) {
        DeployerLauncher deployerLauncher=new DeployerLauncher();

        deployerLauncher.init();
        ParserManage.getInstance().addAnalyser("deployer",new DeployerAnalyser());

        TaskManager.getInstance().addTask(new DeployTask());

        deployerLauncher.startApp();

    }
}
