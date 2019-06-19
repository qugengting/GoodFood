package com.common.library.webview.vassonic;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.common.library.webview.AgentWebFragment;
import com.just.agentweb.MiddlewareWebClientBase;

import static com.common.library.webview.vassonic.SonicJavaScriptInterface.PARAM_CLICK_TIME;

/**
 * Created by cenxiaozhong on 2017/12/18.
 * <p>
 * If you wanna use VasSonic to fast open first page , please
 * follow as sample to update your code;
 */

public class VasSonicFragment extends AgentWebFragment {
    private SonicImpl mSonicImpl;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // 1. 首先创建SonicImpl
        mSonicImpl = new SonicImpl(getUrl(), this.getContext());
        // 2. 调用 onCreateSession
        mSonicImpl.onCreateSession();
        //3. 创建AgentWeb ，注意创建AgentWeb的时候应该使用加入SonicWebViewClient中间件
        super.onViewCreated(view, savedInstanceState); // 创建 AgentWeb 注意的 go("") 传入的 mUrl 应该null 或者""
        //4. 注入 JavaScriptInterface
        mAgentWeb.getJsInterfaceHolder().addJavaObject("sonic", new SonicJavaScriptInterface(mSonicImpl.getSonicSessionClient(), new Intent().putExtra(PARAM_CLICK_TIME, System.currentTimeMillis()).putExtra("loadUrlTime", System.currentTimeMillis())));
        //5. 最后绑定AgentWeb
        mSonicImpl.bindAgentWeb(mAgentWeb);

    }

    //在步骤3的时候应该传入给AgentWeb
    @Override
    public MiddlewareWebClientBase getMiddlewareWebClient() {
        return mSonicImpl.createSonicClientMiddleWare();
    }

    @Override
    public String getUrl() {
        return "https://m.vip.com/?source=www&jump_https=1";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //销毁SonicSession
        if (mSonicImpl != null) {
            mSonicImpl.destrory();
        }
    }
}
