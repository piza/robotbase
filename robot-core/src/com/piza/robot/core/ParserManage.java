package com.piza.robot.core;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Peter on 16/9/28.
 */
public class ParserManage {

    private static final Logger logger= Logger.getLogger(ParserManage.class);

    private static volatile ParserManage parserManager=null;

    private Map<String,IAnalyser> analyserPool=new ConcurrentHashMap<>();

    private ParserManage(){}
    public static ParserManage getInstance(){
        if(parserManager==null){
            synchronized (ParserManage.class){
                if(parserManager==null) {
                    parserManager = new ParserManage();
                }
            }
        }
        return parserManager;
    }

    public void addAnalyser(String key,IAnalyser analyser){
        logger.info("add task:"+analyser.getName());
        analyserPool.put(key,analyser);
    }

    public Collection<IAnalyser> getAllAnalyser(){
        return analyserPool.values();
    }
}
