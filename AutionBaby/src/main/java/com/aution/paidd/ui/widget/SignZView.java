package com.aution.paidd.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.framework.core.utils.DisplayUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.aution.paidd.R;

/**
 * z字型的布局
 * Created by quxiang on 2017/9/4.
 */

public class SignZView extends ViewGroup {

	int child_w;
	int child_h;
	Paint paint = new Paint();

	int lineMoveX;//变化的x

	public SignZView(Context context) {
		this(context,null,0);
	}

	public SignZView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SignZView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setWillNotDraw(false);
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);//设置为空心
		paint.setStrokeWidth(DisplayUtils.dip2px(getContext(),3 ));
	}


	/**
	 * 要求所有的孩子测量自己的大小，然后根据这些孩子的大小完成自己的尺寸测量
	 */
	@SuppressLint("NewApi") @Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec) {
		// 计算出所有的childView的宽和高
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		//测量并保存layout的宽高(使用getDefaultSize时，wrap_content和match_perent都是填充屏幕)
		//稍后会重新写这个方法，能达到wrap_content的效果
		setMeasuredDimension( getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
				getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		final int count = getChildCount();
		int childMeasureWidth = 0;
		int childMeasureHeight = 0;
		int layoutWidth = 0;    // 容器已经占据的宽度
		int layoutHeight = 0;   // 容器已经占据的宽度
		int maxChildHeight = 0; //一行中子控件最高的高度，用于决定下一行高度应该在目前基础上累加多少
		for(int i = 0; i<count; i++){
			View child = getChildAt(i);
			//注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
			childMeasureWidth = child.getMeasuredWidth();
			childMeasureHeight = child.getMeasuredHeight();
			if (child_w==0){
				child_w=childMeasureWidth;
				lineMoveX=child_w/2;
				child_h=childMeasureHeight;
			}
			switch (i){
				case 0:
					left=0;
					top=0;
					right=childMeasureWidth;
					bottom=childMeasureHeight;
					break;
				case 1:
					left=getMeasuredWidth()/2-childMeasureWidth/2;
					top=0;
					right=left+childMeasureWidth;
					bottom=childMeasureHeight;
					break;
				case 2:
					left=getMeasuredWidth()-childMeasureWidth;
					top=0;
					right=left+childMeasureWidth;
					bottom=childMeasureHeight;
					break;
				case 3:
					left=getMeasuredWidth()/2-childMeasureWidth/2;
					top=getMeasuredHeight()/2-childMeasureHeight/2;
					right=left+childMeasureWidth;
					bottom=top+childMeasureHeight;
					break;
				case 4:
					left=0;
					top=getMeasuredHeight()-childMeasureHeight;
					right=childMeasureWidth;
					bottom=getMeasuredHeight();
					break;
				case 5:
					left=getMeasuredWidth()/2-childMeasureWidth/2;
					top=getMeasuredHeight()-childMeasureHeight;
					right=left+childMeasureWidth;
					bottom=top+childMeasureHeight;
					break;
				case 6:
					left=getMeasuredWidth()-childMeasureWidth;
					top=getMeasuredHeight()-childMeasureHeight;
					right=left+childMeasureWidth;
					bottom=top+childMeasureHeight;
					break;
			}

			//确定子控件的位置，四个参数分别代表（左上右下）点的坐标值
			child.layout(left, top, right, bottom);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBgLine(canvas);
	}

	public void drawBgLine(Canvas c){
		paint.setColor(getResources().getColor(R.color.theme_color));
		Path path = new Path();//三角形

		int dp14=DisplayUtils.dip2px(getContext(),7);

		path.moveTo(child_w/2, child_h/2-dp14);
		path.lineTo(getMeasuredWidth()-child_w/2,child_h/2-dp14);
		path.lineTo(child_w/2,getMeasuredHeight()-child_h/2-dp14);
		path.lineTo(getMeasuredWidth()-child_w/2,getMeasuredHeight()-child_h/2-dp14);
		c.drawPath(path, paint);

		drawMoveTo1(c);
	}

	public void moveTo(int index){
		switch (index){
			case 1:
				ObjectAnimator.ofInt(this,"lineMoveX",child_w/2,getMeasuredWidth()/2-child_w/2).start();
				break;
		}
	}


	private void drawMoveTo1(Canvas c){
		paint.setColor(Color.parseColor("#000000"));
		Path path = new Path();//三角形
		path.moveTo(child_w/2, child_h/2);
		path.lineTo(lineMoveX,child_h/2);
		c.drawPath(path, paint);
	}

	private void setLineMoveX(int x){
		lineMoveX=x;
		invalidate();
	}

	private int getLineMoveX(){
		return lineMoveX;
	}

}
