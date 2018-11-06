package com.framework.core.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by quxiang on 2017/2/6.
 */

public class CKItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private int space=1;
    private Paint dividerPaint;

    public CKItemDecoration(int dividerHeight,int color) {
        this.dividerHeight = dividerHeight;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
    }

    public CKItemDecoration(int space,int dividerHeight,int color) {
        this.dividerHeight = dividerHeight;
        dividerPaint = new Paint();
        this.space=space;
        dividerPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (space==1){
            outRect.bottom = dividerHeight;//类似加了一个bottom padding
        }else if (space==2){
            outRect.left=dividerHeight;
            outRect.top=dividerHeight;
            outRect.right=dividerHeight;
            outRect.bottom=dividerHeight;
        }
    }

    int count=1;

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (space==1){
            drawBottonLine(parent,c);
        }else if (space==2){
            drawLeftLine(parent,c);
            drawBottonLine(parent,c);
        }
    }

    public void drawBottonLine(RecyclerView parent,Canvas c){
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    public void drawLeftLine(RecyclerView parent,Canvas c){
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float left=view.getWidth();
            float right=view.getWidth()+dividerHeight;
            float top = 0;
            float bottom = view.getBottom();
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
