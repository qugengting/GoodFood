package com.qugengting.goodfood;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xuruibin on 2018/10/16.
 * 描述：专门用于测试的界面
 * 内存优化，参考：http://liuwangshu.cn/application/performance/ram-3-memory-leak.html
 */
public class TestActivity extends BaseActivity {
    private static Object inner;

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
//                        //接受证书
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }
        });

        mWebView.loadUrl("https://www.huxiu.com/channel/2.html");//https://www.2247bb.com   https://www.6604yy.com
    }

//    @OnClick(R.id.btn_test)
    public void test() {
//        createInnerClass();//内存泄漏一
//        startAsyncTask();//内存泄漏二
//        mHandler.sendMessageDelayed(Message.obtain(), 60000);//内存泄漏三

//        myHandler.sendMessageDelayed(Message.obtain(), 60000);//正确使用方式
//        finish();
        String url = "qugengting://ec.setting";
        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(in);
    }

    private void createInnerClass() {
        class Inner {
        }
        inner = new Inner();
    }

    private void startAsyncTask() {
        new AsyncTask<Void, Void, Void>() {//1
            @Override
            protected Void doInBackground(Void... params) {
                while (true) ;
            }
        }.execute();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void show() {

    }

    private MyHandler myHandler = new MyHandler(this);

    /**
     * Handler正确使用方式
     */
    private static class MyHandler extends Handler {
        private final WeakReference<TestActivity> mActivity;

        public MyHandler(TestActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null && mActivity.get() == null) {
                mActivity.get().show();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
