package com.common.library.util;

/**
 * Created by xuruibin on 2017/12/20.
 * 描述：正则表达式工具类
 */

public class RegularUtils {
    /**
     * 判断首个字符是否是字母
     *
     * @param s
     * @return
     */
    public static boolean isAlphabetAtFirst(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断首个字符是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumericAtFirst(String str) {
        if (Character.isDigit(str.charAt(0))) {
            return true;
        }
        return false;
    }
}
