package com.framework.core.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by quxiang on 2017/2/6.
 */

public class ToastTools {

    private static Toast toast;

    public static void showToast(Context context,String msg){
        if (toast==null){
            toast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

}
