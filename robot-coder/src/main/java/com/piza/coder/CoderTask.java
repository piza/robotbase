package com.piza.coder;

import com.piza.robot.core.ConfigUtil;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 16/9/28.
 */
public class CoderTask extends TaskBase {

    private static final Logger logger= Logger.getLogger(CoderTask.class);

    private volatile static boolean working=false;//make sure just run one task at the sametime

    @Override
    public String getTaskName() {
        return "coderTask";
    }

    @Override
    public void run() {

        if(working){
            this.sendChat("duplicate command!");
            return;
        }
        try {
            working=true;
            List<String> tableList=new ArrayList();
            getTableList(tableList,this.chatMessage.getContent());
            if(tableList.size()==0){
                this.sendChat("wrong param, example:\n code table1 table2 ...");
                return;
            }
            CoderWorker worker=new CoderWorker(tableList);
            worker.generate();

        }finally {
            working=false;
        }
    }

    private void getTableList(List<String> tableList,String command){
        String[] cmdArr=command.split(" ");
        if(cmdArr.length<2){
            return;
        }
        int ind=0;
        for(String tableName:cmdArr){
            if(ind>0){
                tableList.add(tableName);
            }
            ind++;
        }
    }

    class CoderWorker{
        private List<String> tableList;
        public CoderWorker(List<String> tableList){
            this.tableList=tableList;
        }

        public void generate(){
            boolean overwrite = true;
            List<String> warnings = new ArrayList<String>();
            try {
                Configuration configuration = new Configuration();
                configuration.addClasspathEntry(ConfigUtil.getStrProp("coder.classPathEntry"));
                Context context=new Context(ModelType.CONDITIONAL);
                context.setId("defaultContext");


                CommentGeneratorConfiguration commentGeneratorConfiguration=new CommentGeneratorConfiguration();
                commentGeneratorConfiguration.addProperty("suppressAllComments","true");
                context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

                JDBCConnectionConfiguration jdbcConnectionConfiguration=new JDBCConnectionConfiguration();
                jdbcConnectionConfiguration.setDriverClass(ConfigUtil.getStrProp("com.mysql.jdbc.Driver"));
                jdbcConnectionConfiguration.setConnectionURL(ConfigUtil.getStrProp("coder.connectionURL"));
                jdbcConnectionConfiguration.setUserId(ConfigUtil.getStrProp("coder.userId"));
                jdbcConnectionConfiguration.setPassword(ConfigUtil.getStrProp("coder.password"));
                context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

                JavaModelGeneratorConfiguration javaModelGeneratorConfiguration=new JavaModelGeneratorConfiguration();
                javaModelGeneratorConfiguration.setTargetPackage(ConfigUtil.getStrProp("coder.basePackage")+".model");
                javaModelGeneratorConfiguration.setTargetProject(ConfigUtil.getStrProp("coder.outputPath") + "/src/");
                context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

                SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration=new SqlMapGeneratorConfiguration();
                sqlMapGeneratorConfiguration.setTargetPackage("orm");
                sqlMapGeneratorConfiguration.setTargetProject(ConfigUtil.getStrProp("coder.outputPath")+"/resources/");
                context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);


                JavaClientGeneratorConfiguration javaClientGeneratorConfiguration=new JavaClientGeneratorConfiguration();
                javaClientGeneratorConfiguration.setTargetPackage(ConfigUtil.getStrProp("coder.basePackage") + ".dao");
                javaClientGeneratorConfiguration.setTargetProject(ConfigUtil.getStrProp("coder.outputPath") + "/src/");
                context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

                for(String tableName:this.tableList){
                    TableConfiguration tableConfiguration=new TableConfiguration(context);
                    tableConfiguration.setSchema(ConfigUtil.getStrProp("coder.portal_db"));
                    tableConfiguration.setTableName(tableName);
                    tableConfiguration.setDomainObjectName(convertDomainName(tableName));
                    GeneratedKey generatedKey=new GeneratedKey("id","MySql",true,"pre");
                    tableConfiguration.setGeneratedKey(generatedKey);
                    context.addTableConfiguration(tableConfiguration);
                }

                configuration.addContext(context);

                DefaultShellCallback callback = new DefaultShellCallback(overwrite);
                MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
                myBatisGenerator.generate(new TemplateGenerate(configuration));

            }catch (Exception e){
                e.printStackTrace();
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

    }


    class TemplateGenerate implements ProgressCallback {

        private Configuration configuration;
        private VelocityContext context;
        private VelocityEngine ve;
        private String outputRootPath;
        private String baseOutputPath;

        private Template mapperTemplate;
        private Template serviceTemplate;
        private Template implTemplate;
        private Template validatorTemplate;
        private Template controllerTemplate;

        public TemplateGenerate(Configuration configuration){
            logger.info("init TemplateGenerate");
            this.configuration=configuration;
            context = new VelocityContext();
            String basePackage=ConfigUtil.getStrProp("coder.basePackage");
            context.put("basePackage",basePackage);
            context.put("modelPackage",basePackage+".model");
            context.put("daoPackage", basePackage+".dao");
            context.put("servicePackage",basePackage+".service");
            context.put("validatorPackage",basePackage+".validator");
            context.put("controllerPackage",basePackage+".controller");
            context.put("apiPackage",basePackage+".api");

            outputRootPath=ConfigUtil.getStrProp("coder.outputPath");
            baseOutputPath=outputRootPath+ConfigUtil.getStrProp("coder.basePackagePath");
            ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

            mapperTemplate = ve.getTemplate("mapperTemplate.vm");
            serviceTemplate = ve.getTemplate("serviceTemplate.vm");
            implTemplate = ve.getTemplate("serviceImplTemplate.vm");
            validatorTemplate = ve.getTemplate("validatorTemplate.vm");
            controllerTemplate = ve.getTemplate("controllerTemplate.vm");
        }

        @Override
        public void introspectionStarted(int totalTasks) {

        }

        @Override
        public void generationStarted(int totalTasks) {
            logger.info("generationStarted:totalTasks:" + totalTasks);
            checkFolder(outputRootPath, "resources", true);
            checkFolder(outputRootPath, "src", true);
            checkFolder(baseOutputPath, "service", false);
            checkFolder(baseOutputPath,"service"+File.separator+"impl",false);
            checkFolder(baseOutputPath,"validator",false);
            checkFolder(baseOutputPath,"controller",false);

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
            List<TableConfiguration> tableConfigurationList=this.configuration.getContext("defaultContext").getTableConfigurations();

            for(TableConfiguration tableConfiguration:tableConfigurationList){
                String modelClass=tableConfiguration.getDomainObjectName();
                context.put("modelClass", modelClass);
                context.put("modelClassParam", lowFirstChar(modelClass));
                writeTemplate("dao_generics",modelClass,mapperTemplate,"Mapper");
                writeTemplate("service",modelClass,serviceTemplate,"Service");
                writeTemplate("service"+File.separator+"impl",modelClass,implTemplate,"ServiceImpl");
                writeTemplate("validator",modelClass,validatorTemplate,"Validator");
                writeTemplate("controller",modelClass,controllerTemplate,"Controller");

            }

        }

        private String lowFirstChar(String tempStr){
            char[] cs=tempStr.toCharArray();
            cs[0]+=32;
            return String.valueOf(cs);
        }

        private void writeTemplate(String folderName,String modelClass,Template template,String suffix){
            try {
                FileWriter writer=new FileWriter(baseOutputPath+File.separator+folderName+File.separator+modelClass+".java");
                template.merge(context, writer);
                writer.flush();
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        @Override
        public void checkCancel() throws InterruptedException {

        }
    }


}
