package com.qugengting.goodfood;

import android.app.Application;
import android.content.Context;

import com.common.library.net.NetWorkRetrofit;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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

        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(30_000) // set connection timeout.
                        .readTimeout(30_000) // set read timeout.
                ))
                .commit();
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
