package com.common.library.net.bean;

import java.util.List;

/**
 * Created by xuruibin on 2018/2/8.
 * 描述：
 */

public class ReturnResultItem {

    private String title;

    private List<String> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
