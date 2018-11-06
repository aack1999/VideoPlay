package com.framework.core.common;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.core.R;
import com.framework.core.utils.DisplayUtils;

/**
 * Created by quxiang on 2017/4/7.
 */

public class CKDialog extends Dialog{

    public CKDialog(@NonNull Context context) {
        super(context, R.style.mydialog);
        init();
    }

    public void init(){
        RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(DisplayUtils.getScreenW()-DisplayUtils.dip2px(getContext(),100), -2);
        View v= LayoutInflater.from(getContext()).inflate(R.layout.framework_base_dialog,null);
        setContentView(v,ll);

        Button btn_left=(Button)findViewById(R.id.btn_left);
        Button btn_right=(Button)findViewById(R.id.btn_right);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void setContent(String content){
        TextView tv_content=(TextView)findViewById(R.id.tv_content);
        if (tv_content!=null){
            tv_content.setText(content);
        }
    }

}
