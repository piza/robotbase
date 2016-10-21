package com.piza.translate;

import com.piza.robot.core.ConversationTask;
import com.piza.robot.core.FriendData;
import com.piza.robot.core.FriendManage;
import com.piza.robot.util.HttpUtil;
import com.piza.robot.util.JsonUtil;
import org.apache.log4j.Logger;

/**
 * Created by Peter on 2016/10/20.
 */
public class TranslateTask extends ConversationTask {
    private static final Logger logger= Logger.getLogger(TranslateTask.class);

    private static final String TRANSLATE_URL="";

    @Override
    public String getTaskName() {
        return "translateTask";
    }



    @Override
    public ConversationTask newConversation() {

        return new TranslateTask();
    }

    @Override
    public void talk(String msg) {

        String json = "{\"word\":\"" + msg + "\",\"userId\":\""
                + getUserId()
                + "}";
        String transStr= HttpUtil.postJSON(TRANSLATE_URL,json,FriendManage.getInstance().getLoginData().getCurrentToken());
        SimpleResult simpleResult= JsonUtil.str2Obj(transStr,SimpleResult.class);
        if(simpleResult!=null){
            this.sendChat(pretty(simpleResult));
        }

    }

    private String pretty(SimpleResult simpleResult){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(simpleResult.getQuery()).append("\n").append(simpleResult.getPhonetic())
                .append("\n").append(simpleResult.getTranslation())
                .append("\n").append(simpleResult.getExplains());
        if(simpleResult.getWeb()!=null){
            for(String web:simpleResult.getWeb()){
                stringBuilder.append("\n").append(web);
            }
        }
        return stringBuilder.toString();
    }

}
