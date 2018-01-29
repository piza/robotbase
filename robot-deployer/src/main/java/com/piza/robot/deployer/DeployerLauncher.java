package com.piza.robot.deployer;

import com.piza.jjp.JJPAnalyser;
import com.piza.jjp.JJPTask;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.Launcher;
import com.piza.robot.core.ParserManage;
import com.piza.robot.core.TaskManager;
import com.piza.translate.TranslateAnalyser;
import com.piza.translate.TranslateTask;
import com.piza.ysl.YslAnalyser;
import com.piza.ysl.YslTask;
import com.piza.zhiyu.ZhiyuAnalyser;
import com.piza.zhiyu.ZhiyuTask;

/**
 * Created by Peter on 16/9/28.
 */
public class DeployerLauncher extends Launcher {



    public static void main(String[] args) {
        DeployerLauncher deployerLauncher=new DeployerLauncher();

        deployerLauncher.init();

        ConfigUtil.initProp("deployer.properties");

//        ParserManage.getInstance().addAnalyser(new DeployPocketAnalyser());
//
//        TaskManager.getInstance().addTask(new DeployPocketItem());

//        ConfigUtil.initProp("coder.properties");
//        ParserManage.getInstance().addAnalyser(new CoderAnalyser());
//        TaskManager.getInstance().addTask(new CoderTask());

//
//        ParserManage.getInstance().addAnalyser(new DeployerEshowAnalyser());
//        TaskManager.getInstance().addTask(new DeployEshowTask());




//        ParserManage.getInstance().addAnalyser(new TranslateAnalyser());
//        TaskManager.getInstance().addTask(new TranslateTask());


//        ConfigUtil.initProp("zhiyu.properties");
//        ParserManage.getInstance().addAnalyser(new ZhiyuAnalyser());
//        TaskManager.getInstance().addTask(new ZhiyuTask());


//        ConfigUtil.initProp("jjp.properties");
//        ParserManage.getInstance().addAnalyser(new JJPAnalyser());
//        TaskManager.getInstance().addTask(new JJPTask());

        ConfigUtil.initProp("ysl.properties");
        ParserManage.getInstance().addAnalyser(new YslAnalyser());
        TaskManager.getInstance().addTask(new YslTask());

        deployerLauncher.startApp();

    }
}
