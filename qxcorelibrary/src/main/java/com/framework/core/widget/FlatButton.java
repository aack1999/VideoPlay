package com.framework.core.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.framework.core.R;
import com.framework.core.utils.DisplayUtils;


/**
 *统一按钮
 *
 * @Author: chenkai
 * @Date: 2016/5/30
 * @Time 15:18
 */
public class FlatButton extends TextView {

    float radius;

    int backgroundcolor;

    int textcolor;

    int textcolor_press;

    int fillColor;

    int type;//0=正常样式，1=线框样式

    public FlatButton(Context context) {
        super(context);
        init(null);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        setClickable(true);
        setGravity(Gravity.CENTER);
        TypedArray ta=getContext().obtainStyledAttributes(attrs, R.styleable.FlatButton);
        radius=ta.getDimension(R.styleable.FlatButton_fb_radius, DisplayUtils.dip2px(getContext(),4));
        backgroundcolor=ta.getColor(R.styleable.FlatButton_fb_backgroundcolor, ContextCompat.getColor(getContext(),R.color.theme_color));
        textcolor=ta.getColor(R.styleable.FlatButton_fb_textcolor,0);
        type=ta.getInteger(R.styleable.FlatButton_fb_type,0);
        fillColor=ta.getColor(R.styleable.FlatButton_fillColor,Color.TRANSPARENT);
        textcolor_press=ta.getColor(R.styleable.FlatButton_fb_textcolor_press, 0);
        ta.recycle();
        setTextColor(textcolor!=0?textcolor:Color.WHITE);
        setTextSize(14);
        switch (type){
            case 0:
                setBackgroundDrawable(getNormalButton());
                break;
            case 1:
                setBackgroundDrawable(getLineButton());
                break;
        }
    }

    /**
     * 正常按钮样式
     * @return
     */
    public StateListDrawable getNormalButton(){
        ShapeDrawable normal=getRoundRectShape(backgroundcolor);

        ShapeDrawable press1=getRoundRectShape(backgroundcolor);
        ShapeDrawable press2=getRoundRectShape(Color.parseColor("#22000000"));
        Drawable d[]={press1,press2};
        LayerDrawable press =new LayerDrawable(d);

        ShapeDrawable disable=getRoundRectShape(Color.parseColor("#cccccc"));

        StateListDrawable sld=new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled},press);
        sld.addState(new int[]{android.R.attr.state_focused,android.R.attr.state_enabled},press);
        sld.addState(new int[]{android.R.attr.state_enabled},normal);
        sld.addState(new int[]{-android.R.attr.state_enabled},disable);
        return sld;
    }

    /**
     * 线框按钮样式
     * @return
     */
    public StateListDrawable getLineButton(){
        GradientDrawable normal=getLineDrawable(backgroundcolor);
        GradientDrawable disable=getLineDrawable(Color.parseColor("#cccccc"));
        ShapeDrawable press1=getRoundRectShape(backgroundcolor);
        Drawable d[]={press1};
        LayerDrawable press =new LayerDrawable(d);
        StateListDrawable sld=new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled},press);
        sld.addState(new int[]{android.R.attr.state_focused,android.R.attr.state_enabled},press);
        sld.addState(new int[]{android.R.attr.state_enabled},normal);
        sld.addState(new int[]{-android.R.attr.state_enabled},disable);
        setTextColor(createColorStateList(textcolor!=0?textcolor:backgroundcolor,textcolor_press!=0?textcolor_press:Color.WHITE,textcolor_press!=0?textcolor_press:Color.WHITE,Color.parseColor("#cccccc")));
        return sld;
    }

    private float[] getOuterRadius(){
        return new float[]{radius,radius,radius,radius,radius,radius,radius,radius};
    }

    private ShapeDrawable getRoundRectShape(int color){
        ShapeDrawable sd=new ShapeDrawable(new RoundRectShape(getOuterRadius(),null,null));
        sd.getPaint().setColor(color);
        return sd;
    }

    private GradientDrawable getLineDrawable(int backgroundcolor){
        int strokeWidth = DisplayUtils.dip2px(getContext(),1); // 3dp 边框宽度
        float roundRadius = DisplayUtils.dip2px(getContext(),4); // 8dp 圆角半径
        if (radius!=0){
            roundRadius=radius;
        }
        int strokeColor = backgroundcolor;//边框颜色
        int fillColor = this.fillColor;//内部填充颜色

        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);

        return gd;
    }

    /** 对TextView设置不同状态时其文字颜色。 */
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public void setBackgroundcolor(int color){
        backgroundcolor=color;
        switch (type){
            case 0:
                setBackgroundDrawable(getNormalButton());
                break;
            case 1:
                setBackgroundDrawable(getLineButton());
                break;
        }
    }

}
