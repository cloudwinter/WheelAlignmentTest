package com.inprintech.wheelalignmenttest.Utils;

import android.text.TextUtils;

/**
 * Created by xiayundong on 2020/4/10.
 */
public class ValidateUtils {
    /**
     * 判断参数中是否有位空的参数
     *
     * @param params
     * @return
     */
    public static boolean isEmpty(String... params) {
        boolean result = false;
        for (int i = 0; i < params.length; i++) {
            if (TextUtils.isEmpty(params[i])) {
                result = true;
                break;
            }
        }
        return result;
    }
}
