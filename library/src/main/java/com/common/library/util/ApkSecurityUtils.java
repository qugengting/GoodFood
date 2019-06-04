package com.common.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author:xuruibin
 * @date:2019/6/3
 * Description: apk安全工具类，主要有classes.dex完整性校验和签名校验
 * 不管这两者使用哪一种，如果不加固代码很容易被反编译，校验的代码也一览无余
 * 加固平台：https://wetest.qq.com/console/safety/reinforcement
 * 加固完，classes.dex完整性校验会失败，所以只能用签名校验了
 * 同时，string资源文件任何情况下都能被反编译看到，所以签名校验的数值最好放在代码当中
 */
public class ApkSecurityUtils {
    //验证apk中classes.dex文件的crc32的值,即对dex文件进行完整性校验
    public static boolean checkDexCrcValue(Context context) {
        String apkPath = context.getPackageCodePath();
        Long dexCrc = 4217641047L;
        try {
            ZipFile zipfile = new ZipFile(apkPath);
            ZipEntry dexentry = zipfile.getEntry("classes.dex");
            Log.e("checkDexCrcValue", "classes.dexcrc = " + dexentry.getCrc());
            if (dexentry.getCrc() == dexCrc) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取签名
     *
     * @param context
     * @return
     */
    public static String getSignature(Context context, String type) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        StringBuilder sb = new StringBuilder();

        try {
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            for (Signature signature : signatures) {
                String tmp = "";
                if (type.equals(SHA1)) {
                    tmp = getSignatureString(signature, SHA1);
                } else if (type.equals(MD5)) {
                    tmp = getSignatureString(signature, MD5);
                } else if (type.equals(SHA256)) {
                    tmp = getSignatureString(signature, SHA256);
                }
                sb.append(tmp);
                Log.e("getSignature", "tmp = " + tmp);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static boolean verifySignature(Context context, String type) {
        String currentSign = getSignature(context, type);
        Log.e("verifySignature", "currentSign = " + currentSign);
        if ("46a17d01dfe39bab9904ec51f07497acb4c31bd4".equals(currentSign)) {
            return true;//签名正确
        }
        return false;//签名被篡改
    }








    public final static String SHA1 = "SHA1";
    public final static String MD5 = "MD5";
    public final static String SHA256 = "SHA256";

    /**
     * 获取相应的类型的字符串（把签名的byte[]信息转换成16进制）
     *
     * @param sig
     * @param type
     *
     * @return
     */
    public static String getSignatureString(Signature sig, String type) {
        byte[] hexBytes = sig.toByteArray();
        String fingerprint = "error!";
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest != null) {
                byte[] digestBytes = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (byte digestByte : digestBytes) {
                    sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
                }
                fingerprint = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return fingerprint;
    }
}
