package com.framework.core.imp;

import android.content.Context;
import android.widget.ImageView;

import com.framework.core.model.BannerBean;
import com.youth.banner.loader.ImageLoader;

import me.xiaopan.sketch.SketchImageView;

/**
 * Created by quxiang on 2017/4/18.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        BannerBean bean=(BannerBean) path;
        SketchImageView siv=(SketchImageView) imageView;
        siv.displayImage(bean.getImg());
    }

    @Override
    public ImageView createImageView(Context context) {
        return new SketchImageView(context);
    }
}
