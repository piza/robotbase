package com.piza;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest {


    public static void main(String[] args) {

        String filePath="/Users/piza/work/ysl/db_design/ysl-2018-1-29.sql";
        String new_filePath="/Users/piza/work/ysl/db_design/ysl-2018-1-29_new.sql";
        File file=new File(filePath);
        File fileNew=new File(new_filePath);

        String pattern="`.*`";
        Pattern r = Pattern.compile(pattern);
        try {
            List<String> allLines=FileUtils.readLines(file);
            List<String> newLines=new ArrayList();

            for(String lineStr:allLines){
                Matcher m = r.matcher(lineStr);
                if(m.find()){
                    System.out.println("Found value: " + m.group(0));
                    lineStr=lineStr.replace(m.group(0),replace(m.group(0)));
                    System.out.println(lineStr);
                }
                newLines.add(lineStr);
            }

            FileUtils.writeLines(fileNew,newLines);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String replace(String s){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<s.length();i++){
            char c=s.charAt(i);
            if(Character.isUpperCase(c)){
                stringBuilder.append("_");
            }
            stringBuilder.append(Character.toLowerCase(c));
        }
        return stringBuilder.toString();
    }
}
