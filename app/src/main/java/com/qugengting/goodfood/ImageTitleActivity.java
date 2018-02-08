package com.qugengting.goodfood;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.common.library.net.NetWorkRetrofit;
import com.common.library.net.bean.ReturnResult;
import com.common.library.util.Utils;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;
import com.google.gson.Gson;
import com.qugengting.goodfood.adapter.ImageTitleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuruibin on 2018/2/7.
 * 描述：图片日期分类列表
 */

public class ImageTitleActivity extends AppCompatActivity {
    private static final String TAG = ImageTitleActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.lv_image_title)
    ListView listView;
    protected ProgressDialog dialog;
    private ImageTitleAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        setContentView(R.layout.activity_image_title);
        ButterKnife.bind(this);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new ImageTitleAdapter(this, list);
        listView.setAdapter(adapter);

        getImageTitles();
    }

    private void getImageTitles() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage("加载中...");
        dialog.show();
        NetWorkRetrofit.getInstance().getServiceAPI().getImageTitles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReturnResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Utils.makeText(ImageTitleActivity.this, "error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ReturnResult s) {
                        if (s != null && s.isSuccess()) {
                            Utils.makeText(ImageTitleActivity.this, s.getMessage());
                            adapter.setData(s.getList());
                        } else {
                            Utils.makeText(ImageTitleActivity.this, "没有数据");
                        }
                        Gson gson = new Gson();
                        String result = gson.toJson(s);
                        Log.e(TAG, result);
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

}
