package com.piza;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Peter on 2017/10/8.
 */
public class TestAddCode {


    public static void main(String[] args) {
        String modelClass="/Users/hong/tmp/ActionItem.java";

        File modelFile=new File(modelClass);

        try {
            List<String> codeList= FileUtils.readLines(modelFile,"UTF-8");
            List<Integer> addList=new ArrayList();
            for(int i=0;i<codeList.size();i++){
                if(codeList.get(i).contains("private Date")){
                   addList.add(i);
                }
            }
            if(addList.size()>0){
                codeList.add(2,"import com.fasterxml.jackson.annotation.JsonFormat;");

                int offset=1;
                for(Integer index:addList){
                    codeList.add(index+offset,"    @JsonFormat(pattern=\"yyyy-MM-dd HH:mm:ss\", timezone=\"GMT+8\")");
                    offset++;
                }
                FileUtils.writeLines(modelFile,codeList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
