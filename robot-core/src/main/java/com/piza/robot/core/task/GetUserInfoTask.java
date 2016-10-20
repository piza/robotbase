package com.piza.robot.core.task;

import com.piza.robot.core.ChatMessage;
import com.piza.robot.core.ConfigUtil;
import com.piza.robot.core.TaskBase;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Set;


/**
 * Created by Peter on 2016/10/20.
 */
public class GetUserInfoTask extends TaskBase {

    private static final Logger logger= Logger.getLogger(GetUserInfoTask.class);

    private Roster roster;

    public GetUserInfoTask(Roster roster){
        this.roster=roster;
        ChatMessage chatMessage1=new ChatMessage();
        chatMessage1.setFriend(ConfigUtil.getStrProp("adminAccount"));
    }

    @Override
    public void run() {
       logger.info("start get user info task");
        Set<RosterEntry> allEntry=roster.getEntries();
        for(RosterEntry rosterEntry:allEntry){
            System.out.println("----------");
            System.out.println("user:"+rosterEntry.getUser());
            System.out.println("name:"+rosterEntry.getName());
            System.out.println("type"+rosterEntry.getType());
            System.out.println("status"+rosterEntry.getStatus());
        }

    }

    @Override
    public String getTaskName() {
        return "getUserInfoTask";
    }
}
