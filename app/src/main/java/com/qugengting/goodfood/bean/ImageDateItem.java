package com.qugengting.goodfood.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xuruibin on 2018/2/8.
 * 描述：
 */

public class ImageDateItem extends DataSupport {

    private String imageDateName;

    public String getImageDateName() {
        return imageDateName;
    }

    public void setImageDateName(String imageDateName) {
        this.imageDateName = imageDateName;
    }

    private List<String> imageList;

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
