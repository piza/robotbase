package com.piza.robot.core;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Peter on 16/9/27.
 */
public class ChatService {

    private static final Logger logger= Logger.getLogger(ChatService.class);
    private static volatile ChatService chatService=null;
    private Executor executor;
    private BlockingQueue<ChatMessage> messagesQueue;
    private ChatRobot chatRobot;
    private CommandRobot commandRobot;

    private ChatService(){
        logger.info("init ChatService");
        executor=Executors.newFixedThreadPool(2);
        messagesQueue=new LinkedBlockingQueue<ChatMessage>();
        String chatModel=ConfigUtil.getStrProp("chatModel");
        if(chatModel.equals("cmd") || chatModel.equals("both")){
            commandRobot=new CommandRobot(messagesQueue);
        }
        if(chatModel.equals("jabber") || chatModel.equals("both") ){
            chatRobot=new ChatRobot(messagesQueue);
        }
    }

    public static ChatService getInstance(){
        if(chatService==null){
            synchronized (ChatService.class){
                if(chatService==null) {
                    chatService = new ChatService();
                }
            }
        }
        return chatService;
    }

    public void start(){
        if(chatRobot!=null){
            executor.execute(chatRobot);
        }
        if(commandRobot!=null){
            executor.execute(commandRobot);
        }
    }

    public String report(){
        StringBuilder sb=new StringBuilder();
        sb.append("robot status:\n");
        if(chatRobot!=null){
            executor.execute(chatRobot);
        }
        return sb.toString();
    }

    public void sendMessage(String to,String content){
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setContent(content);
        chatMessage.setFriend(to);
        messagesQueue.add(chatMessage);
    }


}
