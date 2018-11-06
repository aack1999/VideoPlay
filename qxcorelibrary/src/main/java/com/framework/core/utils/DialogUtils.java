package com.framework.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by aacc on 2015/7/27.
 */
public class DialogUtils {

    public static void showMessage(Context context,String text){
        new MaterialDialog.Builder(context).title("提示").content(text).positiveText("确定").show();
    }

    public static void showDialog(Context context,String text,String btn, final View.OnClickListener onClickListener){
        new MaterialDialog.Builder(context).title("提示").content(text).negativeText("取消").positiveText(btn).callback(new MaterialDialog.ButtonCallback() {

            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                onClickListener.onClick(null);
            }
        }).show();
    }



}
