package com.common.library.image;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.common.library.R;
import com.common.library.glide.ProgressInterceptor;
import com.common.library.glide.ProgressListener;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import java.io.File;

/**
 * Created by xuruibin on 2018/3/7.
 * 描述：
 */

public class LargeImageViewActivity extends AppCompatActivity {
    private ToolBar toolBar;
    private LargeImageView largeImageView;
    private String url = "http://img6.3lian.com/c23/desk4/07/01/d/09.jpg";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        setContentView(R.layout.activity_largeimageview);
        toolBar = findViewById(R.id.toolbar);
        largeImageView = findViewById(R.id.largeview);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("加载中");
        //添加下载监听
        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                progressDialog.setProgress(progress);
            }
        });
        //方式一：
//        Glide.with(this)
//                .load(url)
//                .downloadOnly(new DownloadImageTarget());
        //方式二：
        downloadImage();
    }

    private  class DownloadImageTarget implements Target<File> {
        private static final String TAG = "DownloadImageTarget";
        @Override
        public void onStart() {
            progressDialog.show();
        }
        @Override
        public void onStop() {
        }
        @Override
        public void onDestroy() {
        }
        @Override
        public void onLoadStarted(Drawable placeholder) {
        }
        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            progressDialog.dismiss();
        }
        @Override
        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
            progressDialog.dismiss();
            ProgressInterceptor.removeListener(url);

            Log.d(TAG, resource.getPath());
            largeImageView.setImage(new FileBitmapDecoderFactory(resource));
        }
        @Override
        public void onLoadCleared(Drawable placeholder) {
        }
        @Override
        public void getSize(SizeReadyCallback cb) {
            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        }
        @Override
        public void setRequest(Request request) {
        }
        @Override
        public Request getRequest() {
            return null;
        }
    }

    public void downloadImage() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Context context = getApplicationContext();
                    FutureTarget<File> target = Glide.with(context)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    final File imageFile = target.get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            ProgressInterceptor.removeListener(url);
                            largeImageView.setImage(new FileBitmapDecoderFactory(imageFile));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
