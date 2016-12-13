package com.piza.festival;

import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.ShellJob;
import com.piza.robot.core.TaskBase;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 16/9/28.
 */
public class FestivalTask extends TaskBase {

    private static final Logger logger= Logger.getLogger(FestivalTask.class);

    private volatile static boolean working=false;//make sure just run one task at the sametime

    @Override
    public String getTaskName() {
        return "festivalTask";
    }

    @Override
    public void run() {

        if(working){
            this.sendChat("duplicate command!");
            return;
        }
        try {
            working=true;
            if(this.hasTaskItem("code")){
                CodeTaskItem codeTaskItem=new CodeTaskItem(this);
                codeTaskItem.work();
            }else if(this.hasTaskItem("deployAll")){
                (new DeployHtmlItem(this,false)).work();
                (new DeployTaskItem(this)).work();
            }else if(this.hasTaskItem("deploy")){
                (new DeployTaskItem(this)).work();
            }else if(this.hasTaskItem("html")){
                (new DeployHtmlItem(this)).work();
            }else if(this.hasTaskItem("admin")){
                (new DeployAdminItem(this)).work();
            }else{
                this.sendChat("help:\n code       generate code\n deploy    deploy project" +
                        "\n html    deploy wechat html project" +
                        "\n admin    deploy admin project" +
                        "\n skipBuild    skip build" +
                        "\n skipPull     skip pull code");
            }

        }finally {
            working=false;
        }
    }



}
