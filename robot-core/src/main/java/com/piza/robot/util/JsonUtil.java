package com.piza.robot.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.*;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将JSON字符串转换为对象
     *
     * @param json
     *            JSON字符串
     * @param valueType
     *            对象类型
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        if(StringUtils.isEmpty(json) || valueType==null){
            return null;
        }
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String json, TypeReference<?> typeReference) {
        if(StringUtils.isEmpty(json) || typeReference==null){
            return null;
        }
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T str2Obj(String content,
                                Class<T> cls) {
        if (StringUtils.isNotBlank(content)) {
            return (T) JSONObject.toBean(JSONObject.fromObject(content), cls);
        }
        return null;
    }

    public static <T> T str2Obj(String content, JsonConfig jsonConfig) {
        if (StringUtils.isNotBlank(content)) {
            return (T) JSONObject.toBean(JSONObject.fromObject(content), jsonConfig);
        }
        return null;
    }

    public static JSONObject getObj(String content) {
        if (StringUtils.isNotBlank(content))
            return JSONObject.fromObject(content);
        return null;
    }

    public static Integer getGameCode(String content) {
        if (StringUtils.isNotBlank(content)) {
            JSONObject obj = JSONObject.fromObject(content);
            return obj.getInt("code");
        }
        return -1000;
    }

    public static String obj2JsonString(Object obj) {
        return JSONObject.fromObject(obj).toString();
    }

    public static String array2JsonString(Object obj) {
        return JSONArray.fromObject(obj).toString();
    }

    public static <T> List<T> toList(String content,
                                     Class<T> cls) {
        return (List<T>) JSONArray.toCollection(JSONArray.fromObject(content), cls);
    }

    public static void main(String[] args) {
        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        for (int i = 24; i <= 28; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("day", i);
            map.put("havedata", 0);
            list.add(map);
        }
        Random r = new Random();
        for (int i = 1; i <= 31; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("day", i);
            map.put("havedata", 1);
            map.put("nowfee", 3216 + r.nextInt(1000000));
            map.put("oldfee", 3216 + r.nextInt(1000000));
            map.put("dau", 3216 + r.nextInt(1000000));
            map.put("dnu", 3216 + r.nextInt(1000000));
            list.add(map);
        }
        for (int i = 1; i <= 6; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("day", i);
            map.put("havedata", 0);
            list.add(map);
        }
        System.out.println(array2JsonString(list));

        String json = "{\"code\":0,\"info\":\"success\",\"data\":{\"systemMail\":{\"mailVer\":19,\"target\":0,\"condition\":0,\"value\":2,\"title\":\"???è?????é??\",\"content\":\"???è?????é?????è?????é??\",\"sendTime\":1402555848,\"award\":{\"awardId\":0,\"gold\":2,\"heart\":2,\"diamond\":333,\"items\":[12,123],\"itemsCount\":[1,2]}}},\"sys_time\":1402555848}";
        JSONObject obj = getObj(json);
        System.out.println(obj.getJSONObject("data").getJSONObject("systemMail").getInt("mailVer"));
        System.out.println(obj.getInt("code"));

    }

}
