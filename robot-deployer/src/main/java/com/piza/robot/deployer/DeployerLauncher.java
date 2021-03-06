package com.piza.robot.deployer;

import com.piza.bus.BusAnalyser;
import com.piza.bus.BusTask;
import com.piza.hkld.HkldAnalyser;
import com.piza.hkld.HkldTask;
import com.piza.robot.core.*;
/**
 * Created by Peter on 16/9/28.
 */
public class DeployerLauncher extends Launcher {


    @Override
    public void init() {
        super.init();

        timer.schedule(new SelfCheckTimerTask(),600000,600000);//execute every 10 minutes

    }

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
//
//
//        ConfigUtil.initProp("wine.properties");
//        ParserManage.getInstance().addAnalyser(new WineAnalyser());
//        TaskManager.getInstance().addTask(new WineTask());
//
        ConfigUtil.initProp("hkld.properties");
        ParserManage.getInstance().addAnalyser(new HkldAnalyser());
        TaskManager.getInstance().addTask(new HkldTask());

        ConfigUtil.initProp("bus.properties");
        ParserManage.getInstance().addAnalyser(new BusAnalyser());
        TaskManager.getInstance().addTask(new BusTask());

//        ConfigUtil.initProp("water.properties");
//        ParserManage.getInstance().addAnalyser(new WaterAnalyser());
//        TaskManager.getInstance().addTask(new WaterTask());
//        ConfigUtil.initProp("cpc.properties");
//        ParserManage.getInstance().addAnalyser(new CpcAnalyser());
//        TaskManager.getInstance().addTask(new CpcTask());

//        ConfigUtil.initProp("ysl.properties");
//        ParserManage.getInstance().addAnalyser(new YslAnalyser());
//        TaskManager.getInstance().addTask(new YslTask());

        deployerLauncher.startApp();

    }
}
