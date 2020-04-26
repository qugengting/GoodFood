package com.qugengting.gaode;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author:xuruibin
 * @date:2020/4/26 Description:
 */
public class LocationUtils {
    public static void getLocationInfo(final Context context) {

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setOnceLocation(true);
        AMapLocationClient mlocationClient = new AMapLocationClient(context);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    Log.e("location", "定位数据：" + amapLocation.toString());
                    Toast.makeText(context, "定位地址：" + amapLocation.getAddress(), Toast.LENGTH_LONG).show();
                } else {
                    Log.e("location", "定位错误：" + amapLocation.toString());
                }
            }
        });
        mlocationClient.startLocation();
    }
}
