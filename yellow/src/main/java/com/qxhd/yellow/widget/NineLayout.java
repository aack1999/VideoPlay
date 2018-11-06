package com.qxhd.yellow.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninegrid.ImageInfo;
import com.ninegrid.NineGridViewAdapter;
import com.ninegrid.NineGridViewWrapper;
import com.qxhd.yellow.model.NineLayoutInof;

import java.util.List;

import me.xiaopan.sketch.SketchImageView;

public class NineLayout extends ViewGroup {

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度

    private int gridSpacing = 3;                    // 宫格间距，单位dp

    List<NineLayoutInof> mImageInfo;

    public NineLayout(Context context) {
        super(context);
    }

    public NineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImageInfo != null && mImageInfo.size() > 0) {
            gridWidth = gridHeight = (totalWidth - gridSpacing ) / 2;
            width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
            height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
        }
        Log.e("wefesfsdfs",width+"   "+height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mImageInfo == null) return;
        int childrenCount = mImageInfo.size();
        for (int i = 0; i < childrenCount; i++) {
            ViewGroup childrenView = (ViewGroup)getChildAt(i);
            SketchImageView img=(SketchImageView)childrenView.getChildAt(0);
            img.displayImage(mImageInfo.get(i).getImg());
            TextView text=(TextView) childrenView.getChildAt(1);
            text.setText(mImageInfo.get(i).getTitle());
            int rowNum = i / columnCount;
            int columnNum = i % columnCount;
            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            childrenView.layout(left, top, right, bottom);
        }
    }


    public void setData(List data) {
        mImageInfo = data;
        columnCount=2;
        rowCount=data.size()/columnCount;
        requestLayout();
    }

}
