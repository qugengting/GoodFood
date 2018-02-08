package com.common.library.net;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.common.library.net.api.ServiceAPI;
import com.common.library.net.interceptor.AddCookiesInterceptor;
import com.common.library.net.interceptor.LoggingInterceptor;
import com.common.library.net.interceptor.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuruibin on 2017/11/6.
 * 描述：
 */

public class NetWorkRetrofit {
    public static final String BASE_URL = "http://172.20.106.45:8080/ssm-crud-myexercise/";
    /**
     * okhttp3拦截器，需增加依赖compile 'com.squareup.okhttp3:logging-interceptor:3.1.2'，
     * 看起来好像没有自定义{@link LoggingInterceptor}的好用
     */
    private static final HttpLoggingInterceptor HTTP_LOGGING_INTERCEPTOR = new HttpLoggingInterceptor(
            new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("xuruibin", "HTTP_LOGGING_INTERCEPTOR: " + message);
                }
            }
    ).setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient;

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(
            RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));

    private NetWorkRetrofit() {

    }

    public void init(Context context) {
        httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(1000 * 3, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor(context))
                .addInterceptor(new AddCookiesInterceptor(context));
    }

    private static class geneInstance {
        private static final NetWorkRetrofit INSTANCE = new NetWorkRetrofit();
    }

    public static NetWorkRetrofit getInstance() {
        return geneInstance.INSTANCE;
    }

    public ServiceAPI getServiceAPI() {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(ServiceAPI.class);
    }

}
