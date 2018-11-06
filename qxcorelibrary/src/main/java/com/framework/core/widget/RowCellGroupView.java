package com.framework.core.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framework.core.R;
import com.framework.core.tools.ACache;
import com.framework.core.utils.DisplayUtils;
import com.zcw.togglebutton.ToggleButton;

import java.util.List;

import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import me.xiaopan.sketch.shaper.CircleImageShaper;

/**
 * Created by quxiang on 2017/3/28.
 */

public class RowCellGroupView extends LinearLayout {

    List<RowCell> cells;

    OnClickListener onClickListener;

    int marginLeft = 50;

    int itemHeight=48;

    SketchImageView logo_img;

    public RowCellGroupView(Context context) {
        this(context, null, 0);
    }

    public RowCellGroupView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowCellGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0xffffffff);
        setOrientation(VERTICAL);
    }

    public void notifyDataChanged(List<RowCell> cells) {
        this.cells = cells;
        if (cells == null || cells.size() <= 0) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < cells.size(); i++) {
            RowCell cell = cells.get(i);
            if (cell == null) {
                View space = new View(getContext());
                space.setBackgroundColor(getResources().getColor(R.color.base_app_bg));
                LayoutParams ll = new LayoutParams(-1, DisplayUtils.dip2px(getContext(), 10));
                space.setLayoutParams(ll);
                addView(space);
            } else {
                if (cell instanceof LogoRowCell) {
                    addView(createLogoRowView((LogoRowCell) cell));
                } else {
                    addView(createRowView(cell));
                }
                if (i + 1 < cells.size() && cells.get(i + 1) != null) {
                    addView(createLineView(DisplayUtils.dip2px(getContext(),marginLeft )));
                }
            }
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    private View createRowView(final RowCell rowcell) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.base_row_cell_layout, null);
        view.setId(rowcell.getViewid());
        TextView label = (TextView) view.findViewById(R.id.row_label);
        TextView desc = (TextView) view.findViewById(R.id.row_desc);
        ImageView img = (ImageView) view.findViewById(R.id.row_img);
        ImageView arrow = (ImageView) view.findViewById(R.id.row_arrow);
        ToggleButton right_toggle = (ToggleButton) view.findViewById(R.id.right_toggle);
        if (!TextUtils.isEmpty(rowcell.getLabel())) {
            label.setVisibility(VISIBLE);
            label.setText(rowcell.getLabel());
        } else {
            label.setVisibility(GONE);
        }

        if (rowcell.getResid() > 0) {
            img.setVisibility(VISIBLE);
            img.setImageResource(rowcell.getResid());
            marginLeft = itemHeight/2+18;
        } else {
            img.setVisibility(GONE);
            marginLeft = 20;
        }

        if (!TextUtils.isEmpty(rowcell.getDesc())) {
            desc.setVisibility(VISIBLE);
            desc.setText(rowcell.getDesc());
        } else {
            desc.setVisibility(GONE);
        }

        if (rowcell.isEnableSwicth()) {
            right_toggle.setVisibility(VISIBLE);
            arrow.setVisibility(GONE);
            right_toggle.toggle(rowcell.isSwicth());
            if (rowcell.isSwicth()){
                right_toggle.setToggleOn(false);
            }else {
                right_toggle.setToggleOff(false);
            }
            right_toggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    if (onClickListener != null) {
                        view.setTag(on);
                        onClickListener.onClick(view);
                    }
                }
            });
        } else {
            right_toggle.setVisibility(GONE);
            arrow.setVisibility(rowcell.isAction() ? VISIBLE : INVISIBLE);
            view.setClickable(rowcell.isAction());
            if (rowcell.isAction()) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickListener != null) {
                            onClickListener.onClick(view);
                        }
                    }
                });
            }
        }
        view.setLayoutParams(new LayoutParams(-1, DisplayUtils.dip2px(getContext(), itemHeight)));
        return view;
    }

    private View createLogoRowView(LogoRowCell rowcell) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.logo_row_cell_layout, null);
        view.setId(rowcell.getViewid());
        TextView label = (TextView) view.findViewById(R.id.row_label);
        logo_img = (SketchImageView) view.findViewById(R.id.row_img);
        ImageView arrow = (ImageView) view.findViewById(R.id.row_arrow);
        if (!TextUtils.isEmpty(rowcell.getLabel())) {
            label.setVisibility(VISIBLE);
            label.setText(rowcell.getLabel());
        } else {
            label.setVisibility(GONE);
        }
        marginLeft = 20;
        String path= getLogoFile();


        DisplayOptions options = new DisplayOptions();
        options.setImageShaper(new CircleImageShaper());
        logo_img.setOptions(options);

        if (!TextUtils.isEmpty(path)){
            logo_img.displayImage(path);
        }else {
            logo_img.displayImage(rowcell.getImgUrl());
        }

        arrow.setVisibility(rowcell.isAction() ? VISIBLE : GONE);
        view.setClickable(rowcell.isAction());
        if (rowcell.isAction()) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                }
            });
        }
        view.setLayoutParams(new LayoutParams(-1, DisplayUtils.dip2px(getContext(), 80)));
        return view;
    }

    public View createLineView(int marginleft) {
        View view = new View(getContext());
        LayoutParams ll = new LayoutParams(-1, DisplayUtils.dip2px(getContext(), 0.5f));
        ll.setMargins(marginleft, 0, 0, 0);
        view.setLayoutParams(ll);
        view.setBackgroundColor(0xffdddddd);
        return view;
    }

    public String getLogoFile(){
        return ACache.get(getContext()).getAsString("logoFile");
    }


    public SketchImageView getLogoImg(){
        return logo_img;
    }
}
