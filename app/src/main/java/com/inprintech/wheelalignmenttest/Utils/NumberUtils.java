package com.inprintech.wheelalignmenttest.Utils;

import android.text.TextUtils;

public class NumberUtils {

    /**
     * double 转 str 保留1位小数,四舍五入
     * @param num
     * @return
     */
    public static String double2Str(double num) {
        return String.format("%.2f", num);
    }

    /**
     *
     * @param numStr
     * @return
     */
    public static double str2double(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0;
        }
        return Double.valueOf(numStr);
    }
}
