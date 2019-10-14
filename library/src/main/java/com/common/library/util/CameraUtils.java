package com.common.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * @author:xuruibin
 * @date:2019/10/14 Description:调用系统相机辅助类，参考：https://www.jianshu.com/p/2820a10b8b85
 * 拍完有些机型的照片有旋转角度，需调整，参考：https://www.cnblogs.com/lucktian/p/7028111.html
 */
public class CameraUtils {

    public static String sCameraPath;

    public static Uri getOutputMediaFileUri(Context context) {
        File mediaFile = null;
        try {
            File mediaStorageDir = new File(context.getExternalCacheDir().getAbsolutePath() +
                    File.separator + "Pictures");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }

            mediaFile = new File(mediaStorageDir.getPath()
                    + File.separator
                    + "temp.jpg");//注意这里需要和filepaths.xml中配置的一样
            sCameraPath = mediaFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// sdk >= 24  android7.0以上
            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",//与清单文件中android:authorities的值保持一致
                    mediaFile);//FileProvider方式或者ContentProvider。也可使用VmPolicy方式
            return contentUri;

        } else {
            return Uri.fromFile(mediaFile);//或者 Uri.isPaise("file://"+file.toString()
        }
    }

    /**
     * 获取正确的旋转角度的图片——一般由系统相机拍照才会导致此情况
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static Bitmap getBitmapWithRightRotation(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.e("CameraUtils", "degree: " + degree);
        Bitmap bm = BitmapFactory.decodeFile(path);
        //照片没有被旋转角度，直接返回原图片
        if (degree == 0) {
            return bm;
        }
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

}
