package com.common.library.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by xuruibin on 2017/11/10.
 * 描述：统一holder抽象类
 */

public abstract  class BaseHolder {
    public BaseHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
