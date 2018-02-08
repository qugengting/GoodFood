package com.qugengting.goodfood.eventbus;

/**
 * Created by xuruibin on 2018/2/8.
 * 描述：
 */

public class ImageEvent {
    private String imageTitle;

    public ImageEvent(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }
}
