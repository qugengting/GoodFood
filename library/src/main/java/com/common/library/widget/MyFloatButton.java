package com.common.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.common.library.R;

/**
 * Created by xuruibin on 2018/12/4.
 * 描述：
 */
public class MyFloatButton extends AbastractDragFloatActionButton {
    public MyFloatButton(Context context) {
        super(context);
    }

    public MyFloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_float_button;//拿到你自己定义的悬浮布局
    }

    @Override
    public void renderView(View view) {
        //初始化那些布局
    }
}
