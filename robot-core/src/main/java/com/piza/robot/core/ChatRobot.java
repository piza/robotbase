package com.piza.robot.core;

import com.piza.robot.core.task.GetUserInfoTask;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
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

/**
 * Created by Peter on 2016/10/3.
 */
public class ChatRobot implements Runnable{

    private static final Logger logger= Logger.getLogger(ChatRobot.class);

    private final ChatRobotListener listener;

    private XMPPTCPConnection connection;

    private ChatManager chatManager;

    private BlockingQueue<ChatMessage> messagesQueue;

    private Roster roster;

    public ChatRobot( BlockingQueue<ChatMessage> messagesQueue){
        this.messagesQueue=messagesQueue;
        listener=new ChatRobotListener();
    }

    public void checkStatu(){
        if(!connection.isConnected()){
            login();
        }
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
         roster=Roster.getInstanceFor(connection);
         GetUserInfoTask getUserInfoTask=new GetUserInfoTask(roster);
         TaskManager.getInstance().addTask(getUserInfoTask);

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
        connection.addConnectionListener(new ConnectionListenerImpl());
        connection.setPacketReplyTimeout(60000);
        connection.connect();
        connection.login(ConfigUtil.getStrProp("GtalkRobotID"), ConfigUtil.getStrProp("GtalkPassword"));

    }

    public boolean addFriend(String email, String name) {
        boolean isSuccess=false;
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

    public class ChatRobotListener implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {

            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setFriend(chat.getParticipant());
            chatMessage.setContent(message.getBody());
            DispatchService.getInstance().add(chatMessage);

        }

    }


    public class ConnectionListenerImpl implements ConnectionListener{
        @Override
        public void connected(XMPPConnection connection) {
            logger.info("connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            logger.info("authenticated:"+resumed);
        }

        @Override
        public void connectionClosed() {
            logger.info("connectionClosed");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            logger.info("connectionClosedOnError:"+e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void reconnectionSuccessful() {
            logger.info("reconnectionSuccessful");
        }

        @Override
        public void reconnectingIn(int seconds) {
            logger.info("reconnectingIn:"+seconds);
        }

        @Override
        public void reconnectionFailed(Exception e) {
            logger.info("reconnectionFailed:"+e.getMessage());
            e.printStackTrace();
        }
    }
}
