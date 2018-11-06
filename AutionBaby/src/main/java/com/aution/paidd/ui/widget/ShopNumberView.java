package com.aution.paidd.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.aution.paidd.R;


/**
 * Created by quxiang on 2017/1/12.
 */

public class ShopNumberView extends FrameLayout implements View.OnClickListener{

    ImageView add;
    EditText ed_value;
    ImageView remove;
    View temp_value;
    int max;
    int min=1;
    int value=1;
    boolean isCanEdit=false;

    ShopNumberValueChangeListener shopNumberValueChangeListener;


    public ShopNumberView(Context context) {
        this(context,null,0);
    }

    public ShopNumberView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShopNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.shop_number_layout,this);
        add=(ImageView) findViewById(R.id.add);
        remove=(ImageView) findViewById(R.id.remove);
        ed_value=(EditText) findViewById(R.id.ed_value);
        temp_value=findViewById(R.id.temp_value);
        add.setOnClickListener(this);
        remove.setOnClickListener(this);
        temp_value.setOnClickListener(this);
        ed_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(getEditView().getText().toString())){
                    value=1;
                    ed_value.setText(value+"");
                    ed_value.selectAll();
                    return;
                }
                value=Integer.parseInt(getEditView().getText().toString());
                if (value>max){
                    setValue(max);
                    ed_value.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setValue(int value){
        this.value=value;
        if (value>=max){
            this.value=max;
        }else if (value<=min){
            this.value=min;
        }
        ed_value.setText(this.value+"");
        ObjectAnimator obj=ObjectAnimator.ofFloat(ed_value, "scaleY", 1, 2f);
        obj.setRepeatMode(2);
        obj.setInterpolator(new OvershootInterpolator());
        obj.setRepeatCount(1);
        obj.setDuration(200);
        obj.start();
        ObjectAnimator obj1=ObjectAnimator.ofFloat(ed_value, "scaleX", 1, 2f);
        obj1.setRepeatMode(2);
        obj1.setRepeatCount(1);
        obj1.setInterpolator(new OvershootInterpolator());
        obj1.setDuration(200);
        obj1.start();
        if (shopNumberValueChangeListener!=null){
            shopNumberValueChangeListener.onChange(this.value);
        }
    }

    public void setCanEdit(boolean canEdit){
        isCanEdit=canEdit;
        temp_value.setVisibility(canEdit?GONE:VISIBLE);
        ed_value.setEnabled(canEdit);
    }

    public EditText getEditView(){
        return ed_value;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                setValue(++value);
                if (isCanEdit){
                    ed_value.selectAll();
                }
                break;
            case R.id.remove:
                setValue(--value);
                if (isCanEdit){
                    ed_value.selectAll();
                }
                break;
        }
    }



    public void setMaxValue(int value){
        max=value;
    }

    public int getMax() {
        return max;
    }

    public void setMinValue(int value){
        min=value;
    }

    public int getValue() {
        return value;
    }

    public void setShopNumberValueChangeListener(ShopNumberValueChangeListener shopNumberValueChangeListener) {
        this.shopNumberValueChangeListener = shopNumberValueChangeListener;
    }

    public interface ShopNumberValueChangeListener{
        public void onChange(int value);
    }

}
