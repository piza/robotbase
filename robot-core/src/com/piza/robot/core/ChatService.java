package com.piza.robot.core;

import com.piza.robot.core.chat.*;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.concurrent.*;

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


    private class ChatRobot implements Runnable{
        private final GtalkRobotListener listener;

        private XMPPTCPConnection connection;

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

            ChatManager manager=ChatManager.getInstanceFor(connection);


            manager.addChatListener(new ChatManagerListener() {

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

//            XMPPConnection.DEBUG_ENABLED = false;
            //connection = new XMPPConnection(ConvUtil.getProperty("GtalkDomain"));
//            ConnectionConfiguration.Builder config=new ConnectionConfiguration.Builder();
             //.(ConfigUtil.getStrProp("GtalkIP"),ConfigUtil.getIntProp("GtalkPort"),ConfigUtil.getStrProp("GtalkDomain"));
//        config.setCompressionEnabled(true);
//        config.setSASLAuthenticationEnabled(false);

            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                    .builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setServiceName(ConfigUtil.getStrProp("GtalkDomain"));
            config.setHost(ConfigUtil.getStrProp("GtalkIP"));
            config.setPort(ConfigUtil.getIntProp("GtalkPort"));
            config.setDebuggerEnabled(true);
//            XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
            XMPPTCPConnection.setUseStreamManagementDefault(true);
            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            connection = new XMPPTCPConnection(config.build());
            connection.connect();
            connection.login(ConfigUtil.getStrProp("GtalkRobotID"), ConfigUtil.getStrProp("GtalkPassword"));

        }

//        public synchronized boolean addFriend(String email, String name) {
//            boolean isSuccess=false;
//            Roster roster=connection.getRoster();
//            try {
//                roster.createEntry(email, name, null);
//                isSuccess=true;
//            } catch (XMPPException e) {
//                isSuccess=false;
//                e.printStackTrace();
//            }
//
//            return isSuccess;
//        }

//        @Override
//        public boolean localContainFriend(String emailAddress) {
//            Roster roster=connection.getRoster();
//            return roster.contains(emailAddress);
//        }

//        @Override
//        public Map<String, String> localGetFriendList() {
//            Roster roster=connection.getRoster();
//            Map<String,String> fList=new HashMap<String,String>();
//            for(RosterEntry entry:roster.getEntries()){
//                fList.put(entry.getUser(), entry.getName());
//            }
//            return fList;
//        }

//        @Override
//        public String localGetOnlineStatus(String emailAddress) {
//            String status="";
//            Presence pres=connection.getRoster().getPresence(emailAddress);
//            if(pres.isAvailable() || pres.isAway()){
//                status="ONLINE";
//            }else{
//                //status="OFFLINE";
//                status="ONLINE";
//            }
//            return status;
//        }

//        @Override
//        public String localGetDisplayName(String emailAddress) {
//            return connection.getRoster().getEntry(emailAddress).getName();
//        }


        public boolean checkLogin() {
            if(this.connection!=null && this.connection.isConnected()){
                return true;
            }else{
                return this.login();
            }

        }

        public String getName() {
            return "GtalkRobot";
        }
        @Override
        public void run() {
            this.login();
        }
    }


    public class GtalkRobotListener implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            ContainerPool pool=ContainerPool.getPool();
            AbstractDispatcher gtalkDispatcher=pool.getDispatcher();
            if(gtalkDispatcher==null){
                gtalkDispatcher=new GtalkDispatcher();
            }
            UserRequest request=new GtalkUserRequest();
            request.setAttribute(Constants.GTALK_CHAT,chat);
            request.setAttribute(Constants.GTALK_MESSAGE, message);

            UserResponse response=new GtalkUserResponse();
            response.setAttribute(Constants.GTALK_CHAT, chat);
            gtalkDispatcher.setRequest(request);
            gtalkDispatcher.setResponse(response);
            pool.prcessMessage(gtalkDispatcher);

        }

    }


}
