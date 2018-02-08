package com.common.library.net.api;

import com.common.library.net.bean.Category;
import com.common.library.net.bean.ReturnResult;
import com.common.library.net.bean.Shop;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xuruibin on 2017/11/10.
 * 描述：
 */

public interface ServiceAPI {
    @POST("add")
    Observable<ReturnResult> add(@Body Category category);

    @GET("jsontest/getShop")
    Observable<Shop> getShop(@Query("shopName") String shopName, @Query("staffName") String staffName);

    @Multipart
    @POST("file/onefile")
    Observable<ReturnResult> uploadImage(@Part() MultipartBody.Part file);//单张图片上传

    //登录
    @GET("jsontest/login")
    Observable<ReturnResult> login(@Query("account") String shopName, @Query("password") String staffName);

    @GET("jsontest/getImageList")
    Observable<ReturnResult> getImageList(@Query("fileName") String fileName);

    @GET("jsontest/initImages")
    Observable<ReturnResult> initImages();

    @GET("jsontest/getImageTitles")
    Observable<ReturnResult> getImageTitles();
}
