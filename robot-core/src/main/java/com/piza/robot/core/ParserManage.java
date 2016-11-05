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

    public String report(){
        StringBuilder sb=new StringBuilder();
        sb.append("---all parser---\n");
        for(String parserName:analyserPool.keySet()){
            sb.append(parserName).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public void addAnalyser(IAnalyser analyser){
        logger.info("add parser:"+analyser.getName());
        analyserPool.put(analyser.getName(),analyser);
    }

    public Collection<IAnalyser> getAllAnalyser(){
        return analyserPool.values();
    }
}
