package test.tool;

import com.piza.robot.core.ConfigUtil;

import java.io.IOException;

public class BusFeTools {


    public static void main(String[] args) {

        String storePath="E:\\tem\\";
        ConfigUtil.initProp("db.properties");


        FeConfig feConfig=new FeConfig();

        feConfig.setFolderName(storePath);
        feConfig.setDetailsTemp("template-water/fe-details.vm");
        feConfig.setJdbcUrl(ConfigUtil.getStrProp("dbUrl"));
        feConfig.setFormTemp("template-water/fe-form.vm");
        feConfig.setMainTemp("template-water/fe-main.vm");

        FeModule feModule=new FeModule();
        feModule.init(feConfig);


        feModule.setParam("membershipCardManage",
                "充值记录管理",
                "充值记录管理",
                "op_membership_card");

        feModule.generate(FeModule.mainTemplate,"Main");
        feModule.generate(FeModule.formTemplate,"Form");
        feModule.generate(FeModule.detailsTemplate,"Details");

//        ProcessBuilder pb = new ProcessBuilder(new String[]{"/bin/bash", "-c", "open "+storePath});
        ProcessBuilder pb = new ProcessBuilder(new String[]{ "start "+storePath});

        try {
            final Process pid = pb.start();
            pid.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
