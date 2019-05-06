package test.tool;

import org.apache.commons.lang.ArrayUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FeModule {


    private VelocityContext context;
    private VelocityEngine ve;
    public static Template mainTemplate;
    public static Template formTemplate;
    public static Template detailsTemplate;

    private Connection connection=null;
    private String filePrefix;

    private FeConfig feConfig;


    public void init(FeConfig feConfig){
        this.feConfig=feConfig;
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        ve.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

        mainTemplate = ve.getTemplate(feConfig.getMainTemp(),"utf-8");
        formTemplate = ve.getTemplate(feConfig.getFormTemp(),"utf-8");
        detailsTemplate = ve.getTemplate(feConfig.getDetailsTemp(),"utf-8");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(feConfig.getJdbcUrl());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    /**
     *
     * @param permissionPrefix   例:  staff_manage-staff 现在变成内部跳转链接的开头部分了，例如 agentManage
     * @param moduleName   例: 员工管理
     * @param modelName    例: 员工
     * @param tableName
     */
    public void setParam(String permissionPrefix,String moduleName,String modelName,String tableName){
        context = new VelocityContext();
        context.put("permissionPrefix",permissionPrefix);
        context.put("moduleName",moduleName);
        String lowerCaseName=dbToJava(tableName,false);
        System.out.println("lowerCaseName:"+lowerCaseName);
        context.put("lowerCaseName",lowerCaseName);

        System.out.println("modelName:"+modelName);
        context.put("modelName",modelName);

        filePrefix=dbToJava(tableName,true);
        context.put("filePrefix",filePrefix);
        System.out.println("filePrefix: "+filePrefix);
        Statement stmt = null;
        List<String> propList=new ArrayList<>();
        try {
            stmt = connection.createStatement();
            String sql = "desc "+tableName;
            ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
            while (rs.next()) {
                String propName=dbToJava(rs.getString(1),false);
                if(ignoreProp(propName)){
                    continue;
                }
                propList.add(propName);
            }
            System.out.println(ArrayUtils.toString(propList));
            context.put("propList",propList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean ignoreProp(String propName){
        if(propName.equals("id") || propName.equals("isDelete") || propName.equals("updateTime") ||  propName.equals("updateTime") || propName.equals("orgId") ){
            return true;
        }
        return false;
    }
    public void generate(Template template,String suffix ){
        try {
            String filePath=feConfig.getFolderName()+filePrefix+suffix+".vue";
            Writer fstream = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
            template.merge(context, fstream);
            fstream.flush();
            fstream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String dbToJava(String dbCol,boolean firstUper){
        if(dbCol.contains("_")){
            boolean toUper=firstUper;
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<dbCol.length();i++){
                char c=dbCol.charAt(i);
                if(c == '_'){
                    toUper=true;
                    continue;
                }
                if(toUper){
                    stringBuilder.append(Character.toUpperCase(c));
                    toUper=false;
                    continue;
                }
                stringBuilder.append(c);
            }
            return stringBuilder.toString();
        }
        return dbCol;

    }



}
