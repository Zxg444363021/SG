package com.globalformulae.shiguang.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/31.
 */

public class HtmlPageAnalyseUtil {

    private String source, page;
    private Matcher matcher;
    private Pattern pattern;

    //分析与初始化
    public HtmlPageAnalyseUtil(String str1, String str2) {
        this.source = str1.toString();
        this.page = str2.toString();
        setData();
    }
    public HtmlPageAnalyseUtil(){}

    public String getScorce() {
        return source;
    }

    public void setScorce(String scorce) {
        this.source = scorce;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    //正则表达式初始化
    private void setData() {
        pattern = Pattern.compile(source);
        matcher = pattern.matcher(page);
    }

    //获取所有的group
    public List<String> getGroup() {
        List<String> str = new ArrayList<>();
        //int i = 0;
        while(matcher.find()) {
            str.add(matcher.group(1));
            //i++;
        }
        return str;
    }

    //获取指定的group
    public String getGroup(int index) {
        List<String> str = new ArrayList<>();
        //int i = 0;
        while(matcher.find()) {
            str.add(matcher.group(1));
            //i++;
        }
        return str.get(index);
    }

    //获取group的数据个数
    public int getSize() {
        int j = 0;
        while(matcher.find()) {
            j++;
        }
        return j;
    }

}
