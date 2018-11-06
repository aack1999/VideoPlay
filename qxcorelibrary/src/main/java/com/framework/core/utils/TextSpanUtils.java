package com.framework.core.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 *
 * Created by quxiang on 2017/1/12.
 */

public class TextSpanUtils {

    /**
     * 创建start-end 的文字颜色
     * @param temp
     * @param start
     * @param end
     * @param color
     * @return
     */
    public static SpannableString createColorText(String temp,int start,int end,int color){
        SpannableString ss=new SpannableString(temp);
        ss.setSpan(new ForegroundColorSpan(color),start,end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

}
