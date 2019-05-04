package com.piza.cpc;

import com.piza.robot.core.BaseItem;
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
 * Created by Peter on 2016/12/2.
 *
 */
public class OrmTaskItem extends BaseItem {

    private static final Logger logger= Logger.getLogger(OrmTaskItem.class);

    public OrmTaskItem(TaskBase taskBase) {
        super(taskBase);
    }


    public void work(){
        List<String> tableList=new ArrayList();
        getTableList(tableList,taskBase.getMsgContent());
        if(tableList.size()==0){
//            sendChat("wrong param, example:\n code table1 table2 ...");
            return;
        }
        //update code to latest
        String pullCmd =  "pullProject.sh "+ConfigUtil.getStrProp("cpc.projectDir");
        pullCode(pullCmd);

        if(!this.generate(tableList)){
            return;
        }

        commitCode();
    }


    private boolean commitCode(){
        sendChat("start to commit code!");
        String workingDir= ConfigUtil.getStrProp("workDir");

        try {
            String pullCmd = workingDir + File.separator + "commitProject.sh "+ConfigUtil.getStrProp("cpc.projectDir");
            ShellJob shellJob=new ShellJob();
            shellJob.runCommand(pullCmd);
            sendChat("["+shellJob.isSuccess()+"]"+shellJob.getResult());
            if(shellJob.getResult()!=null && shellJob.getResult().contains("coder push success")){
                sendChat("code pushed!");
                return true;
            }
            return shellJob.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
            sendChat("error when pull code:" + e.getMessage());
        }
        return false;
    }


    private void getTableList(List<String> tableList,String command){
        String[] cmdArr=command.split(" ");
        if(cmdArr.length<3){
            sendChat("wrong command: hint:\n cpc code [tablename1] [tablename2] ...");
            return;
        }
        int ind=0;
        for(String tableName:cmdArr){
            if(ind>1){
                tableList.add(tableName);
            }
            ind++;
        }
    }

    public boolean generate(List<String> tableList){
        logger.info("start generate MBG obj");
        List<String> warnings = new ArrayList<String>();
        try {
            Configuration configuration = new Configuration();
            configuration.addClasspathEntry(ConfigUtil.getStrProp("cpc.classPathEntry"));
            Context context=new Context(ModelType.CONDITIONAL);
            context.setId("defaultContext");


            CommentGeneratorConfiguration commentGeneratorConfiguration=new CommentGeneratorConfiguration();
            commentGeneratorConfiguration.addProperty("suppressAllComments","true");
            context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

            JDBCConnectionConfiguration jdbcConnectionConfiguration=new JDBCConnectionConfiguration();
            jdbcConnectionConfiguration.setDriverClass("com.mysql.jdbc.Driver");
            jdbcConnectionConfiguration.setConnectionURL(ConfigUtil.getStrProp("cpc.connectionURL"));
            jdbcConnectionConfiguration.setUserId(ConfigUtil.getStrProp("cpc.userId"));
            jdbcConnectionConfiguration.setPassword(ConfigUtil.getStrProp("cpc.password"));
            context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

            JavaModelGeneratorConfiguration javaModelGeneratorConfiguration=new JavaModelGeneratorConfiguration();
            javaModelGeneratorConfiguration.setTargetPackage(ConfigUtil.getStrProp("cpc.basePackage")+".model");
            javaModelGeneratorConfiguration.setTargetProject(ConfigUtil.getStrProp("cpc.ormPath") + "/java/");
            context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

            JavaClientGeneratorConfiguration javaClientGeneratorConfiguration=new JavaClientGeneratorConfiguration();
            javaClientGeneratorConfiguration.setTargetPackage(ConfigUtil.getStrProp("cpc.basePackage") + ".dao");
            javaClientGeneratorConfiguration.setTargetProject(ConfigUtil.getStrProp("cpc.ormPath") + "/java/");
            javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
            context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

            SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration=new SqlMapGeneratorConfiguration();
            sqlMapGeneratorConfiguration.setTargetPackage("orm");
            sqlMapGeneratorConfiguration.setTargetProject(ConfigUtil.getStrProp("cpc.ormPath")+"/resources/");
            context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);




            for(String tableName:tableList){
                TableConfiguration tableConfiguration=new TableConfiguration(context);
                tableConfiguration.setSchema(ConfigUtil.getStrProp("cpc.database"));
                tableConfiguration.setTableName(tableName);
                tableConfiguration.setDomainObjectName(convertDomainName(tableName));
                GeneratedKey generatedKey=new GeneratedKey("id","MySql",true,null);
                tableConfiguration.setGeneratedKey(generatedKey);
                context.addTableConfiguration(tableConfiguration);

                cleanFile(ConfigUtil.getStrProp("cpc.ormPath")+"/resources/orm/"+tableConfiguration.getDomainObjectName()+"Mapper.xml");
            }

            configuration.addContext(context);
            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);



            myBatisGenerator.generate(new TemplateGenerate(configuration));

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            sendChat("encounter error when generate code:\n"+e.getMessage());
            return false;
        }
        return true;
    }

    private void cleanFile(String path){
        logger.info("cleanFile:" + path);
        File file=new File(path);
        try {
            if(file.exists()) {
                FileUtils.forceDelete(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendChat("encounterred error when clean:"+path);
            logger.error(e);
        }
    }

    private String convertDomainName(String tableName){
        String className="";
        String[] array = tableName.split("_");
        for (int i = 0; i < array.length; i++) {
            className += array[i].substring(0, 1).toUpperCase() + array[i].substring(1);
        }
        return className;
    }




    class TemplateGenerate implements ProgressCallback {

        private Configuration configuration;
        private VelocityContext context;
        private VelocityEngine ve;
        private String ormPath;
        private String servicePath;
        private String controllerPath;
        private String basePackagePath;
        private String controllerPackagePath;

        private Template mapperTemplate;
        private Template serviceTemplate;
        private Template implTemplate;
        private Template validatorTemplate;
        private Template controllerTemplate;
        private Template apiTemplate;


        public TemplateGenerate(Configuration configuration){
            logger.info("init TemplateGenerate");
            this.configuration=configuration;
            context = new VelocityContext();
            String basePackage=ConfigUtil.getStrProp("cpc.basePackage");
            String controllerPackage=ConfigUtil.getStrProp("cpc.controllerPackage");
            context.put("basePackage",basePackage);
            context.put("modelPackage",basePackage+".model");
            context.put("daoPackage", basePackage+".dao");
            context.put("servicePackage",basePackage+".service");
            context.put("validatorPackage",controllerPackage+".validator");
            context.put("controllerPackage",controllerPackage);
            context.put("apiPackage",basePackage+".api");

            ormPath=ConfigUtil.getStrProp("cpc.ormPath");
            servicePath=ConfigUtil.getStrProp("cpc.servicePath");
            controllerPath=ConfigUtil.getStrProp("cpc.controllerPath");


            basePackagePath=ConfigUtil.getStrProp("cpc.basePackagePath");
            controllerPackagePath=ConfigUtil.getStrProp("cpc.controllerPackagePath");

            ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

            mapperTemplate = ve.getTemplate("template-cpc/mapperTemplate.vm");
            serviceTemplate = ve.getTemplate("template-cpc/serviceTemplate.vm");
            implTemplate = ve.getTemplate("template-cpc/serviceImplTemplate.vm");
            validatorTemplate = ve.getTemplate("template-cpc/validatorTemplate.vm");
            controllerTemplate = ve.getTemplate("template-cpc/controllerTemplate.vm");
            apiTemplate = ve.getTemplate("template-cpc/apiTemplate.vm");

        }

        @Override
        public void introspectionStarted(int totalTasks) {
            logger.info("introspectionStarted:totalTasks:" + totalTasks);

        }

        @Override
        public void generationStarted(int totalTasks) {
            logger.info("generationStarted:totalTasks:" + totalTasks);
//            checkFolder(ormPath, "resources", false);
//            checkFolder(outputRootPath, "src", false);
//            checkFolder(baseOutputPath, "service", false);
//            checkFolder(baseOutputPath,"service"+File.separator+"impl",false);
//            checkFolder(baseOutputPath,"validator",false);
//            checkFolder(baseOutputPath,"controller",false);
        }


        private void checkFolder(String parentPath,String folderName,boolean cleanFolder){
            try {
                File folder=new File(parentPath+File.separator+folderName);
                if(cleanFolder && folder.exists()){
                    FileUtils.deleteDirectory(folder);
                }
                if(!folder.exists()){
                    folder.mkdirs();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void saveStarted(int totalTasks) {

        }

        @Override
        public void startTask(String taskName) {

        }

        @Override
        public void done() {
            logger.info("done");
            sendChat("MBG done, start velocity generator!");
            List<TableConfiguration> tableConfigurationList=this.configuration.getContext("defaultContext").getTableConfigurations();

            for(TableConfiguration tableConfiguration:tableConfigurationList){
                String modelClass=tableConfiguration.getDomainObjectName();
                context.put("modelClass", modelClass);
                context.put("modelClassParam", lowFirstChar(modelClass));
                writeTemplate(ormPath+basePackagePath+File.separator+"dao",modelClass,mapperTemplate,"Mapper");
                writeTemplate(servicePath+basePackagePath+File.separator+"service",modelClass,serviceTemplate,"Service");
                writeTemplate(servicePath+basePackagePath+File.separator+"service"+File.separator+"impl",modelClass,implTemplate,"ServiceImpl");
                writeTemplate(controllerPath+controllerPackagePath+File.separator+"validator",modelClass,validatorTemplate,"Validator");
                writeTemplate(controllerPath+controllerPackagePath+File.separator,modelClass,controllerTemplate,"Controller");
                writeTemplate(controllerPath+controllerPackagePath+File.separator,modelClass,apiTemplate,"Api");
                addDateFormatCode(ormPath+basePackagePath+File.separator+"model"+File.separator+modelClass+".java");
            }

        }

        private String lowFirstChar(String tempStr){
            char[] cs=tempStr.toCharArray();
            cs[0]+=32;
            return String.valueOf(cs);
        }

        private void writeTemplate(String folderName,String modelClass,Template template,String suffix){
            try {
                String filePath=folderName+File.separator+modelClass+suffix+".java";
                File file=new File(filePath);
                if(file.exists() && !"Mapper".equals(suffix)){
                    sendChat("skip exists file:"+file.getName());
                    return;
                }
                FileWriter writer=new FileWriter(filePath);
                template.merge(context, writer);
                writer.flush();
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
                sendChat("encounter error when write file:"+folderName+File.separator+modelClass+suffix);
                logger.error(e);
            }

        }
        @Override
        public void checkCancel() throws InterruptedException {

        }

        public void addDateFormatCode(String modelClass){
            File modelFile=new File(modelClass);

            try {
                List<String> codeList= FileUtils.readLines(modelFile,"UTF-8");
                List<Integer> addList=new ArrayList();
                for(int i=0;i<codeList.size();i++){
                    if(codeList.get(i).contains("private Date")){
                        addList.add(i);
                    }
                }
                if(addList.size()>0){
                    codeList.add(2,"import com.fasterxml.jackson.annotation.JsonFormat;");

                    int offset=1;
                    for(Integer index:addList){
                        codeList.add(index+offset,"    @JsonFormat(pattern=\"yyyy-MM-dd\", timezone=\"GMT+8\")");
                        offset++;
                    }
                    FileUtils.writeLines(modelFile,codeList);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
