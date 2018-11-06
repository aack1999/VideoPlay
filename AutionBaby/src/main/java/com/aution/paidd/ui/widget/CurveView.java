package com.aution.paidd.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by quxiang on 2017/6/13.
 */

public class CurveView extends View {

    Paint mPaint;

    public CurveView(Context context) {
        this(context,null,0);
    }

    public CurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        mPaint=new Paint();

        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.FILL);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.WHITE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 这是一个居中的圆
         */
        float x = -40;
        float y = 0;

        RectF oval = new RectF( x, y+getMeasuredHeight()/2, getWidth()+40 , getHeight()+getMeasuredHeight()/2);
        canvas.drawArc(oval,0,360,true,mPaint);//画圆弧，这个时候，绘制没有经过圆心
    }
}
