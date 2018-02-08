package com.qugengting.goodfood;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
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

import com.bumptech.glide.Glide;
import com.common.library.net.NetWorkRetrofit;
import com.common.library.net.bean.ReturnResult;
import com.common.library.util.Utils;
import com.google.gson.Gson;
import com.qugengting.goodfood.eventbus.ImageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuruibin on 2018/2/7.
 * 描述：
 */

public class ImageActivity extends AppCompatActivity {
    private static final String TAG = ImageActivity.class.getSimpleName();
    @BindView(R.id.iv_heihei)
    ImageView imageView;
    @BindView(R.id.vp_images)
    ViewPager vp;
    private List<View> viewPages = new ArrayList<>();
    private PagerAdapter adapter;
    protected ProgressDialog dialog;
    private String mFileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewPages.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewPages.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewPages.get(position);
                container.addView(view);
                return view;
            }
        };
        vp.setAdapter(adapter);
        EventBus.getDefault().register(this);
//        getImage();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void receiveGetImageEvent(ImageEvent event) {
        if (event != null) {
            mFileName = event.getImageTitle();
            getImage();
        }
    }

    private void getImage() {
        viewPages.clear();
        adapter.notifyDataSetChanged();
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage("加载中...");
        dialog.show();
        NetWorkRetrofit.getInstance().getServiceAPI().getImageList(mFileName)
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
                        Utils.makeText(ImageActivity.this, "error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ReturnResult s) {
                        if (s != null && s.isSuccess()) {
                            Utils.makeText(ImageActivity.this, s.getMessage());
                            List<String> urls = s.getList();
                            LayoutInflater inflater = LayoutInflater.from(ImageActivity.this);
                            for (int i = 0; i < urls.size(); i++) {
                                View view = inflater.inflate(R.layout.view_image, null);
                                viewPages.add(view);
                            }
                            adapter.notifyDataSetChanged();
                            for (int i = 0; i < viewPages.size(); i++) {
                                View view = viewPages.get(i);
                                ImageView imageView = view.findViewById(R.id.iv_haha);
                                Glide.with(ImageActivity.this).load(urls.get(i)).into(imageView);
                            }
                        } else {
                            Utils.makeText(ImageActivity.this, "没有数据");
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
        EventBus.getDefault().unregister(this);
    }
}
