package com.piza.robot.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread safe util
 * Created by Peter on 16/9/27.
 */
public class ConfigUtil {

    private static final Properties props=new Properties();
    private static final ReentrantLock lock=new ReentrantLock();

    /**
     * invoke before thread pool start
     */
    public static void initProp(String fileName){
        InputStream inputStream=ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * return "" if prop not found
     * @param key
     * @return
     */
    public static String getStrProp(String key){
        return props.getProperty(key,"");
    }

    /**
     * return null if prop not found or not a integer
     * @param key
     * @return
     */
    public static Integer getIntProp(String key){
        lock.lock();
        try{
        String str= props.getProperty(key);
            if(str==null){
                return null;
            }else{
                try{
                   return Integer.valueOf(str);
                }catch (Exception e){
                    return null;
                }
            }
        }finally {
            lock.unlock();
        }
    }

}
