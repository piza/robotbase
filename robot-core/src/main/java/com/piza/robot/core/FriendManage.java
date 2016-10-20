package com.piza.robot.core;

import com.piza.robot.LoginData;
import com.piza.robot.util.HttpUtil;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
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
        String loginUrl="http://api.01cun.com/login";
        Map<String,String> postData=new HashMap<String,String>();
        postData.put("account",ConfigUtil.getStrProp("robot.account"));
        postData.put("password",ConfigUtil.getStrProp("robot.password"));
        postData.put("appCode","robot");
String json="{\"account\":\""+ConfigUtil.getStrProp("robot.account")+"\",\"password\":\""
        +ConfigUtil.getStrProp("robot.password")
        +"\",\"appCode\":\"robot\"}";
        String loginResult= HttpUtil.postJSON(loginUrl,json);
        System.out.println(loginResult);
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

    public void addFriend(FriendData friendData){
        logger.info("add friendData:"+friendData.getAccount());
        friendList.put(friendData.getAccount(),friendData);
    }

    public Collection<FriendData> getAllFriend(){
        return friendList.values();
    }

    public static void main(String[] args) {
        ConfigUtil.initProp("config.properties");
        FriendManage.getInstance().login();
    }
}
