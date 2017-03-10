package com.piza.robot.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter on 16/9/27.
 */
public class ChatMessage {

    protected String friend;
    protected String content;

    protected Map<String,Boolean> overwriteItem=new HashMap<String,Boolean>();


    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
