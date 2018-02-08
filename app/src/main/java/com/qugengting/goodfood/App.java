package com.qugengting.goodfood;

import android.app.Application;

import com.common.library.net.NetWorkRetrofit;

/**
 * Created by xuruibin on 2018/2/7.
 * 描述：
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkRetrofit.getInstance().init(this);
    }
}
