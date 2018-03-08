package com.qugengting.goodfood.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xuruibin on 2018/3/8.
 * 描述：
 */

public class GoodFoodFavor extends DataSupport {
    private String url;
    private String html;

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
