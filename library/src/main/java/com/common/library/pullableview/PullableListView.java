package com.common.library.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable {

    public PullableListView(Context context) {
        super(context);
        canPullDown = true;
        canPullUp = true;
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        canPullDown = true;
        canPullUp = true;
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        canPullDown = true;
        canPullUp = true;
    }

    private boolean canPullDown;
    private boolean canPullUp;

    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

    @Override
    public boolean canPullDown() {
        if (!canPullDown) {
            return canPullDown;
        }
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (!canPullUp) {
            return canPullUp;
        }
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}
