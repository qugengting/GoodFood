package com.common.library.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by xuruibin on 2017/11/14.
 * 描述：
 */

public class MyGlideModule implements GlideModule {
    private static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE));
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);//默认格式是RGB_565
        //注意！如果设置成ARGB_8888格式，图片变换框架glide-transformations将失去变换效果
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory(okHttpClient));
    }
}
