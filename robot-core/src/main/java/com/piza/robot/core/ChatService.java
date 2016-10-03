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

    private ChatService(){
        logger.info("init ChatService");
        executor=Executors.newSingleThreadExecutor();
        messagesQueue=new LinkedBlockingQueue<ChatMessage>();
        chatRobot=new ChatRobot();
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
        executor.execute(chatRobot);
    }

    public String report(){
        StringBuilder sb=new StringBuilder();
        sb.append("robot status:\n");
        sb.append(chatRobot.report());
        return sb.toString();
    }

    public void sendMessage(String to,String content){
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setContent(content);
        chatMessage.setFriend(to);
        messagesQueue.add(chatMessage);
    }


    private class ChatRobot implements Runnable{
        private final GtalkRobotListener listener;

        private XMPPTCPConnection connection;

        private ChatManager chatManager;

        public ChatRobot(){
            listener=new GtalkRobotListener();
        }
        public boolean login() {
            logger.info("ChatRobot trying to login....");
            boolean success=false;

            connection=getConnection();
            if(connection==null || !connection.isConnected()){
                return false;
            }

            chatManager=ChatManager.getInstanceFor(connection);


            chatManager.addChatListener(new ChatManagerListener() {

                @Override
                public void chatCreated(Chat chat, boolean arg1) {
                    chat.addMessageListener(listener);

                }
            });

            success=true;

            return success;
        }


        private XMPPTCPConnection getConnection(){
            if(connection!=null && connection.isConnected()){
                return connection;
            }

            int times=0;
            while(times<3 && (connection==null ||!connection.isConnected())){
                try {

                    internalGetConnection();
                } catch (XMPPException ex) {
                    ex.printStackTrace();
                    if(times<3){
                        System.err.println("Encountered errors when connecting to server, will try again!"+times);
                        logger.error("Encountered errors when connecting to server, will try again!"+times);
                    }else{
                        logger.error("Encountered errors when connecting to server:"+ex.toString());
                    }
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                times++;
            }

            return connection;
        }

        private void internalGetConnection() throws XMPPException, IOException, SmackException {

            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                    .builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setServiceName(ConfigUtil.getStrProp("GtalkDomain"));
            config.setHost(ConfigUtil.getStrProp("GtalkIP"));
            config.setPort(ConfigUtil.getIntProp("GtalkPort"));
            config.setDebuggerEnabled(true);
            config.setConnectTimeout(50000);
//            XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
            XMPPTCPConnection.setUseStreamManagementDefault(true);
            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            connection = new XMPPTCPConnection(config.build());
            connection.setPacketReplyTimeout(60000);
            connection.connect();
            connection.login(ConfigUtil.getStrProp("GtalkRobotID"), ConfigUtil.getStrProp("GtalkPassword"));

        }

        public boolean addFriend(String email, String name) {
            boolean isSuccess=false;
            Roster roster=Roster.getInstanceFor(connection);
            try {
                roster.createEntry(email, name, null);
                isSuccess=true;
            } catch (XMPPException e) {
                isSuccess=false;
                e.printStackTrace();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            }

            return isSuccess;
        }

        public boolean checkLogin() {
            if(this.connection!=null && this.connection.isConnected()){
                return true;
            }else{
                return this.login();
            }

        }

        public String report() {
           Roster roster=Roster.getInstanceFor(connection);
            Set<RosterEntry> entrySet=roster.getEntries();
            if(entrySet==null){
                return "no entry";
            }
            StringBuilder sb=new StringBuilder();
            for(RosterEntry entry:entrySet){
                sb.append(entry.getUser()+"\n");
            }

            return sb.toString();
        }
        @Override
        public void run() {
            this.login();
            System.out.println("start polling message");
            while(true) {
                try {
                    ChatMessage chatMessage = messagesQueue.take();
                    Chat chat = chatManager.createChat(chatMessage.getFriend());
                    chat.sendMessage(chatMessage.getContent());
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public class GtalkRobotListener implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {

            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setFriend(chat.getParticipant());
            chatMessage.setContent(message.getBody());
            DispatchService.getInstance().add(chatMessage);

        }

    }


}
