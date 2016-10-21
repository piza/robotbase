package com.piza.robot.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.util.Map;

public class HttpUtil {

    public static String post(String url,
                              String data) {
        String r = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);// 设置连接时间
        PostMethod pm = new PostMethod(url);
        StringRequestEntity entity;
        try {
            Thread.sleep(20L);
            entity = new StringRequestEntity(data, null, "utf8");
            pm.setRequestEntity(entity);
            int status = client.executeMethod(pm);
            if (status == HttpStatus.SC_OK) {
                r = new String(pm.getResponseBodyAsString().getBytes("utf-8"));
            }
        } catch (Exception e) {
        }
        return r;
    }

    public static String postJSON(String url,
                                  String json) {
        return postJSON(url,json,null);
    }
    public static String postJSON(String url,
                              String json,String token) {
        String r = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);// 设置连接时间 
        PostMethod pm = new PostMethod(url);
        pm.setRequestHeader("Content-type", "application/json");
        pm.setRequestHeader("Accept", "application/json");
        if(token!=null){
            pm.setRequestHeader("token", token);
        }
        try {
            StringRequestEntity stringRequestEntity=new StringRequestEntity(json,"application/json","utf-8");
            pm.setRequestEntity(stringRequestEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            int status = client.executeMethod(pm);
            if (status == HttpStatus.SC_OK) {
                r = pm.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(r!=null){
            if(r.startsWith("{\"success\":")){
                r=r.substring(11,r.length()-1);
            }else if(r.startsWith("{\"failed\":")){
                r=r.substring(10,r.length()-1);
            }
        }
        return r;
    }
    public static String getJSON(String url) {
        return postJSON(url,null);
    }
    public static String getJSON(String url,
                                  String token) {
        String r = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);// 设置连接时间
        GetMethod pm = new GetMethod(url);
        pm.setRequestHeader("Content-type", "application/json");
        pm.setRequestHeader("Accept", "application/json");
        if(token!=null){
            pm.setRequestHeader("token", token);
        }
        try {
            int status = client.executeMethod(pm);
            if (status == HttpStatus.SC_OK) {
                r = pm.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(r!=null){
            if(r.startsWith("{\"success\":")){
                r=r.substring(11,r.length()-1);
            }else if(r.startsWith("{\"failed\":")){
                r=r.substring(10,r.length()-1);
            }
        }
        return r;
    }

    public static String get(String url,
                              Map<String, Object> data) {
        String r = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 设置连接时间
        GetMethod pm = new GetMethod(url);
        HttpMethodParams httpMethodParams = new HttpMethodParams();
        pm.setParams(httpMethodParams);
        try {
            if(data!=null) {
                for (String key : data.keySet()) {
                    httpMethodParams.setParameter(key, data.get(key));
                }
            }
            int status = client.executeMethod(pm);
            if (status == HttpStatus.SC_OK) {
                r = pm.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public static String convertToString(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStreamReader.close();
                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

}
