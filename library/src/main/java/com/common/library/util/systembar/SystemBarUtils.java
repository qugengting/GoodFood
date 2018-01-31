package com.common.library.util.systembar;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xuruibin on 2018/1/25.
 * 描述：状态栏工具类
 */

public class SystemBarUtils {
    public static void setTranslucentStatus(Activity ct, int color) {
        Window win = ct.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(ct);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);// 状态栏无背景
        // tintManager.setStatusBarTintResource(R.drawable.img_navbar_bg);
        // tintManager.setNavigationBarTintResource(R.drawable.img_navbar_bg);
        tintManager.setStatusBarTintColor(color);
        tintManager.setNavigationBarTintColor(color);
        final SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        // contentView.setPadding(0, config.getStatusBarHeight(), 0,
        // config.getPixelInsetBottom());
        View contentView = ct.findViewById(android.R.id.content);
        if (contentView != null) {
            //Android4.4以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                contentView.setPadding(0, config.getStatusBarHeight(), 0, config.getPixelInsetBottom());
            }

            /*if (ct instanceof LoginActivity) {
                contentView.setPadding(0, 0, 0, 0);
                tintManager.setStatusBarAlpha(0);
                tintManager.setNavigationBarAlpha(0);
            }

            // yuyc 以下代码解决ChatActivity和WebViewActivity软键盘挡住输入框的问题
            if(ct instanceof ChatActivity || ct instanceof WebViewActivity) {
                contentView.setPadding(0, 0, 0, 0);
            }

            // yuyc 以下代码处理小米的状态栏颜色
            if(ct instanceof WebViewActivity) {
                boolean darkmode = false;
                Class<? extends Window> clazz = win.getClass();
                try {
                    int darkModeFlag = 0;
                    Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                    darkModeFlag = field.getInt(layoutParams);
                    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                    extraFlagField.invoke(win, darkmode ? darkModeFlag : 0, darkModeFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // yuyc 以下代码处理魅族的状态栏颜色
            if(ct instanceof WebViewActivity) {
                boolean dark = false;
                try {
                    WindowManager.LayoutParams lp = win.getAttributes();
                    Field darkFlag = WindowManager.LayoutParams.class
                            .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                    Field meizuFlags = WindowManager.LayoutParams.class
                            .getDeclaredField("meizuFlags");
                    darkFlag.setAccessible(true);
                    meizuFlags.setAccessible(true);
                    int bit = darkFlag.getInt(null);
                    int value = meizuFlags.getInt(lp);
                    if (dark) {
                        value |= bit;
                    } else {
                        value &= ~bit;
                    }
                    meizuFlags.setInt(lp, value);
                    win.setAttributes(lp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
