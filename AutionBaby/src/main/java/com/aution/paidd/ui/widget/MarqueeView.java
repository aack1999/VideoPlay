package com.aution.paidd.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.framework.core.utils.DisplayUtils;
import com.aution.paidd.R;
import com.aution.paidd.bean.HeadLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjie on 2016/11/11.
 */
public class MarqueeView extends ViewFlipper {

    private Context mContext;
    private List<HeadLineBean> notices;
    private boolean isSetAnimDuration = false;
    private OnItemClickListener onItemClickListener;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtils.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        int gravityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        typedArray.recycle();

        setFlipInterval(interval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_mymarquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);


        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_mymarquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }


    // 根据公告字符串列表启动轮播
    public void startWithList(List<HeadLineBean> notices) {
        setNotices(notices);
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();

        for (int i = 0; i < notices.size(); i++) {
            final TextView textView = createTextView(notices.get(i), i);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(finalI, textView);
                    }
                }
            });
            addView(textView);
        }

        if (notices.size() > 1) {
            startFlipping();
        }
        return true;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(final HeadLineBean  text, int position) {
        TextView tv = new TextView(mContext);
        tv.setSingleLine(singleLine);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        final String temp="恭喜"+text.getNickname()+"拍到"+text.getName();

        SpannableString ss=new SpannableString(temp);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_color)),2,text.getNickname().length()+2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_color)),text.getNickname().length()+4,temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(ss);


        return tv;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<HeadLineBean> getNotices() {
        return notices;
    }

    public void setNotices(List<HeadLineBean> notices) {
        this.notices = notices;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }
}
