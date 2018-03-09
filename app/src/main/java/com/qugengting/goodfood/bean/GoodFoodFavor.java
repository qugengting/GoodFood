package com.qugengting.goodfood.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xuruibin on 2018/3/8.
 * 描述：
 */

public class GoodFoodFavor extends DataSupport {
    private String url;
    private String html;
    private String favorDate;
    private String imageUrl;
    private String title;
    private long favorTime;


    public long getFavorTime() {
        return favorTime;
    }

    public void setFavorTime(long favorTime) {
        this.favorTime = favorTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavorDate() {
        return favorDate;
    }

    public void setFavorDate(String favorDate) {
        this.favorDate = favorDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
