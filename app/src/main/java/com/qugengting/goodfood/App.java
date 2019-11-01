package com.qugengting.goodfood;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.common.library.net.NetWorkRetrofit;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;

import org.litepal.LitePal;

/**
 * Created by xuruibin on 2018/2/7.
 * 描述：
 */

public class App extends Application {
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkRetrofit.getInstance().init(this);
        LitePal.initialize(this);
        refWatcher = setupLeakCanary();//内存泄漏自动检测

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
//        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
//                .instanceField("android.view.inputmethod.InputMethodManager$ControlledInputConnectionWrapper", "mParentInputMethodManager")
//                .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
//                .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
//                .instanceField("com.android.internal.policy.PhoneWindow$DecorView", "mContext")
//                .instanceField("android.support.v7.widget.SearchView$SearchAutoComplete", "mContext")
//                .build();
//
//        return LeakCanary.refWatcher(this)
//                .listenerServiceClass(DisplayLeakService.class)
//                .excludedRefs(excludedRefs)
//                .buildAndInstall();
        /**
         * 以上ExcludedRefs的设置无效，没深入了解，改用{@link com.qugengting.goodfood.util.FixMemLeak}
         */

        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        App leakApplication = (App) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
}
