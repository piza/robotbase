package com.piza.translate;

import com.piza.robot.core.ConversationTask;
import org.apache.log4j.Logger;

/**
 * Created by Peter on 2016/10/20.
 */
public class TranslateTask extends ConversationTask {
    private static final Logger logger= Logger.getLogger(TranslateTask.class);

    @Override
    public String getTaskName() {
        return "translateTask";
    }



    @Override
    public ConversationTask newConversation() {
        return new TranslateTask();
    }

    @Override
    public void talk(String msg) {

        this.sendChat("translate:"+msg);
    }
}
