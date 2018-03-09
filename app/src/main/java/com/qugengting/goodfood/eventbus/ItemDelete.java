package com.qugengting.goodfood.eventbus;

/**
 * Created by xuruibin on 2018/3/9.
 * 描述：
 */

public class ItemDelete {
    private int position;

    public int getPosition() {
        return position;
    }

    public ItemDelete(int position) {
        this.position = position;
    }
}
