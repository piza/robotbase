package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Peter on 16/9/28.
 */
public class FriendManage {

    private static final Logger logger= Logger.getLogger(FriendManage.class);

    private static volatile FriendManage friendManage=null;

    private Map<String,FriendData> friendList=new ConcurrentHashMap<>();

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
}
