package com.qugengting.goodfood;

import android.app.Application;

import com.common.library.net.NetWorkRetrofit;

import org.litepal.LitePal;

/**
 * Created by xuruibin on 2018/2/7.
 * 描述：
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkRetrofit.getInstance().init(this);
        LitePal.initialize(this);
    }
}
