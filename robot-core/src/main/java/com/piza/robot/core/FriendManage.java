package com.piza.robot.core;

import com.piza.robot.util.HttpUtil;
import com.piza.robot.util.JsonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Peter on 16/9/28.
 */
public class FriendManage {

    private static final Logger logger= Logger.getLogger(FriendManage.class);

    private static volatile FriendManage friendManage=null;

    private Map<String,FriendData> friendList=new ConcurrentHashMap<String,FriendData>();

    private LoginData loginData;

    private FriendManage(){}
    public static FriendManage getInstance(){
        if(friendManage ==null){
            synchronized (FriendManage.class){
                if(friendManage ==null) {
                    friendManage = new FriendManage();
                }
            }
        }
        return friendManage;
    }

    public void login(){
        try {
            String loginUrl = "http://api.01cun.com/login";
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("account", ConfigUtil.getStrProp("robot.account"));
            postData.put("password", ConfigUtil.getStrProp("robot.password"));
            postData.put("appCode", "robot");
            String json = "{\"account\":\"" + ConfigUtil.getStrProp("robot.account") + "\",\"password\":\""
                    + ConfigUtil.getStrProp("robot.password")
                    + "\",\"appCode\":\"robot\"}";
            String loginResult = HttpUtil.postJSON(loginUrl, json);
            this.loginData = JsonUtil.str2Obj(loginResult, LoginData.class);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("enccountered error when login webserver!\n"+e.getMessage());
        }
    }

    public LoginData getLoginData(){
        return loginData;
    }


    public String report(){
        StringBuilder sb=new StringBuilder();
        sb.append("all friend\n");
        for(String parserName:friendList.keySet()){
            sb.append(parserName).append(",");
        }
        sb.append("\n");
        return sb.toString();
    }

    public synchronized FriendData getFriendData(String friend){
        if(friend.contains("/")){
            friend=friend.split("/")[0];
        }
        if(this.friendList.containsKey(friend)){
            return friendList.get(friend);
        }
        String queryUrl = "http://api.01cun.com/api/v1/userRobot/list?robotAccount="+friend;
        String res=HttpUtil.getJSON(queryUrl, this.loginData.getCurrentToken());

        if(res==null){
            return null;
        }
        JSONObject object=JsonUtil.getObj(res);
        if(object.containsKey("models")){
            JSONArray models=object.getJSONArray("models");
            if(models!=null&&models.size()>0){
                JSONObject userRobotJs=  models.getJSONObject(0);
                UserRobot userRobot=(UserRobot)JSONObject.toBean(userRobotJs,UserRobot.class);
                FriendData friendData=new FriendData();
                friendData.setAccount(friend);
                friendData.setUserId(userRobot.getUserId());
                this.friendList.put(friend,friendData);
                return friendData;
            }
        }

        return null;
    }


    public static void main(String[] args) {
//        ConfigUtil.initProp("config.properties");
//        FriendManage.getInstance().login();

        String r="{\"success\":{\"id\":6,\"account\":\"robot\",\"appCode\":null,\"currentToken\":\"0af6b76e-5bed-4ff4-a01b-103dfea1322b\"}}";
        System.out.println(r.substring(11,r.length()-1));
    }
}
