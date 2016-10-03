package com.piza.robot.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Peter on 2016/10/3.
 */
public class CommandRobot implements Runnable {

    private BlockingQueue<ChatMessage> messagesQueue;

    public CommandRobot(BlockingQueue<ChatMessage> messagesQueue){
        this.messagesQueue=messagesQueue;
    }
    @Override
    public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStreamReader isr = new InputStreamReader(System.in);
                    BufferedReader br = new BufferedReader(isr);
                    String line= br.readLine();
                    while (line!=null){
                        ChatMessage chatMessage=new ChatMessage();
                        chatMessage.setFriend("cmd");
                        chatMessage.setContent(line);
                        DispatchService.getInstance().add(chatMessage);
                        line=br.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("start listening message");
        while(true) {
            try {
                ChatMessage chatMessage = messagesQueue.take();
                System.out.println(chatMessage.getContent());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
