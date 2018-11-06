package com.framework.core.common;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.core.R;
import com.framework.core.utils.DisplayUtils;

/**
 * Created by quxiang on 2017/4/6.
 */

public class WaitDialog extends Dialog{

    View root;
    TextView tv_content;

    public WaitDialog(@NonNull Context context) {
        super(context, R.style.mydialog);
        init();
    }


    private void init() {
        setCanceledOnTouchOutside(false);
        RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(DisplayUtils.getScreenW(), DisplayUtils.getScreenH());
        root = getLayoutInflater().inflate(R.layout.framework_dialog_wait_layout, null);
        tv_content = (TextView) root.findViewById(R.id.dialog_content);
        setContentView(root, ll);

    }

    public void setContent(String text){
        tv_content.setText(text);
    }
}
