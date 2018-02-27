package com.common.gif.bean;

import android.graphics.Bitmap;

/**
 * Created by xuruibin on 2018/2/26.
 * 描述：Bitmap，包含序号，可用于recyclerview适配
 */

public class BitmapIndexWrapper {
    private Bitmap bitmap;
    private int index;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
