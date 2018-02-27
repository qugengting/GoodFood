package com.common.library.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by xuruibin on 2018/2/26.
 * 描述：px和pd转换工具类
 */

public class PxUtil {
    /** dp转换px */
    public static int dip2px(Context context, float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources()
                .getDisplayMetrics());
    }

    /** sp转换px */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources()
                .getDisplayMetrics());
    }
}
