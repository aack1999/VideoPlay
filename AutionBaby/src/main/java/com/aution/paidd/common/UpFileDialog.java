package com.aution.paidd.common;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.aution.paidd.R;

/**
 * Created by quxiang on 2017/3/9.
 */

public class UpFileDialog extends Dialog {

    ArcProgress arc_progress;

    public UpFileDialog(@NonNull Context context) {
        super(context, R.style.mydialog);
        init();
    }

    public void init(){
        setContentView(R.layout.dialog_upfile);
        arc_progress=(ArcProgress) findViewById(R.id.arc_progress);
    }

    public void setProgress(float a){
        arc_progress.setProgress((int)(a*100));
    }
}
