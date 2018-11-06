package com.aution.paidd.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by quxiang on 2016/12/17.
 */
public class DialogUtil {

    public static void showMessage(Context context, String title, String content, String btnText, final View.OnClickListener onClickListener) {
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(title);
        a.setMessage(content);
        a.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onClick(null);
            }
        });
        a.setNegativeButton("取消", null);
        a.show();
    }

    public static void showMessage(Context context, String title, String content) {
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(title);
        a.setMessage(content);
        a.setNegativeButton("取消", null);
        a.show();
    }

    public static void showNetMessage(Context context, String title, String content, String btnText, final View.OnClickListener onClickListener) {
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(title);
        a.setMessage(content);
        a.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        a.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onClickListener.onClick(null);
            }
        });
        a.show();
    }

    public static void showMessage(Context context, String title, String content, String btnText, final View.OnClickListener onClickListener, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(title);
        a.setMessage(content);
        a.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onClick(null);
            }
        });
        a.setNegativeButton("取消", null);
        a.setOnDismissListener(onDismissListener);
        a.show();
    }

}
