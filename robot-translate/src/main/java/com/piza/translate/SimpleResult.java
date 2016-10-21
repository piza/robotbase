package com.piza.translate;

import com.piza.robot.core.WebResponse;

import java.util.List;

/**
 * Created by Peter on 16/9/12.
 */
public class SimpleResult extends WebResponse{

    private Integer translateRecordId;
    private String translation;
    private String explains;
    private String phonetic;
    private String query;
    private List<String> web;


    public Integer getTranslateRecordId() {
        return translateRecordId;
    }

    public void setTranslateRecordId(Integer translateRecordId) {
        this.translateRecordId = translateRecordId;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getWeb() {
        return web;
    }

    public void setWeb(List<String> web) {
        this.web = web;
    }
}
