package com.framework.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.framework.core.utils.imageloader.ImageLoaderUtil;
import com.framework.core.utils.imageloader.glideprogress.ProgressLoadListener;

/**
 * Created by quxiang on 2017/2/15.
 */

public class ImageLoadView extends ImageView{

    public ImageLoadView(Context context) {
        this(context,null,0);
    }

    public ImageLoadView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){

    }

    public void loadImageUrl(String url){
        ImageLoaderUtil.getInstance().loadImageWithProgress(url, this, new ProgressLoadListener() {
            @Override
            public void update(int bytesRead, int contentLength) {

            }

            @Override
            public void onException() {

            }

            @Override
            public void onResourceReady() {

            }
        });
    }

}
