package com.qugengting.goodfood.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.qugengting.goodfood.BuildConfig;

import java.lang.reflect.Field;

/**
 * Created by xuruibin on 2018/10/15.
 * 描述：LeakCanary内存泄漏检测工具关于华为android6.0以上机型InputMethodManager内存泄漏报警的适配
 * 参考：https://www.jianshu.com/p/95242060320f
 */
public class FixMemLeak {
    private static Field field;
    private static boolean hasField = true;

    public static void fixLeak(Context context) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (!hasField) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView"};
        for (String param : arr) {
            try {
                if (field == null) {
                    field = imm.getClass().getDeclaredField(param);
                }
                if (field == null) {
                    hasField = false;
                }
                if (field != null) {
                    field.setAccessible(true);
                    field.set(imm, null);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
