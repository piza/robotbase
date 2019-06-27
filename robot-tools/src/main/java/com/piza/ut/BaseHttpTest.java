package com.piza.ut;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BaseHttpTest {

    protected String token=null;


    public String get(String strUrl) throws Exception{

        if(token==null){
            throw new Exception("未设置token");
        }
        long startTime = System.currentTimeMillis();
        URL url = null;
        InputStream in = null;
        BufferedReader bufferedReader = null;
        StringBuilder res = new StringBuilder();
        HttpURLConnection httpurlconnection = null;
        try {
            url = new URL(strUrl);
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestProperty("token",token);
            httpurlconnection.setRequestProperty("Content-type", "application/json");
            httpurlconnection.setConnectTimeout(30000);
            httpurlconnection.setReadTimeout(30000);
            httpurlconnection.setRequestMethod("GET");
            int code = httpurlconnection.getResponseCode();
            if (code == 200) {
                in = httpurlconnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(in,
                        "UTF-8"));
                String str = bufferedReader.readLine();
                while (str != null) {
                    res.append(str);
                    str = bufferedReader.readLine();
                }
            } else {
                System.out.println("get: code = "+code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("use time: "+(System.currentTimeMillis() - startTime));
        System.err.println(res.toString());
        return prettyJson(res.toString());
    }


    public String post(String strUrl,String jsonForm) throws Exception{

        if(token==null){
            throw new Exception("未设置token");
        }
        long startTime = System.currentTimeMillis();
        System.out.println("strUrl is " + strUrl);

        URL url = null;
        InputStream in = null;
        BufferedReader bufferedReader = null;
        StringBuilder res = new StringBuilder();
        HttpURLConnection httpurlconnection = null;
        try {
            if(strUrl.contains("?")){
                url = new URL(strUrl.substring(0, strUrl.indexOf("?")));
            }else{
                url = new URL(strUrl);
            }
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setConnectTimeout(30000);
            httpurlconnection.setReadTimeout(30000);
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setRequestMethod("POST");
            if(jsonForm!=null){
                httpurlconnection.getOutputStream().write(jsonForm.getBytes("UTF-8"));
            }
            httpurlconnection.getOutputStream().flush();
            httpurlconnection.getOutputStream().close();
            int code = httpurlconnection.getResponseCode();
            if (code == 200) {
                in = httpurlconnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(in,
                        "UTF-8"));
                String str = bufferedReader.readLine();
                while (str != null) {
                    res.append(str);
                    str = bufferedReader.readLine();
                }
            }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("cost time is "
                + (System.currentTimeMillis() - startTime));

        return prettyJson(res.toString());
    }


    public String prettyJson(String jsonStr) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonStr);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

}