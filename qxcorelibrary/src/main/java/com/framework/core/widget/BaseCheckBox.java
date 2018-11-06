package com.framework.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by quxiang on 2017/3/13.
 */

public class BaseCheckBox extends CheckBox {

    public BaseCheckBox(Context context) {
        this(context,null);
    }

    public BaseCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
