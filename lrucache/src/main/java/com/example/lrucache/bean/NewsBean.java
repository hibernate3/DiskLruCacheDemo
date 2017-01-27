package com.example.lrucache.bean;

/**
 * Created by wangyuhang on 2017/1/25.
 */

public class NewsBean {
    public String newsIconUrl;
    public String newsTitle;
    public String newsContent;

    @Override
    public String toString() {
        return "newsIconUrl: " + newsIconUrl + ", newsTitle: " +
                newsTitle + ", newsContent: " + newsContent;
    }
}
