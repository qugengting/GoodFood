package com.common.library.util.encrypt;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/***
 * @ClassName: MD5Util
 * @Description: 加密工具
 * @date 2017年11月21日
 */
public class MD5Util {

    //md5 32位 小写加密
    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    /**
     * 64位MD5加密
     */
    public static String encryptWithBase64(String password) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte b[] = md.digest(password.getBytes());
            result = Base64.encodeToString(b, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 32位MD5加密
     */
    public static String encryptWithBase32(String password) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(password.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();// 加密
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}