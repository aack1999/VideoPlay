package com.aution.paidd.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.aution.paidd.R;
import com.aution.paidd.bean.ShopTypeBean;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by quxiang on 2017/3/3.
 */

public class CategoryTabAdapter extends RecyclerView.Adapter {

    WeakReference<Context> context;
    List<ShopTypeBean> mItems;

    View.OnClickListener onClickListener;

    public CategoryTabAdapter(Context context, List<ShopTypeBean> mItems) {
        this.context = new WeakReference<>(context);
        this.mItems = mItems;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context.get()).inflate(R.layout.fragment_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        View tv_color;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_color = itemView.findViewById(R.id.tv_color);

        }

        public void onBindViewHolder(final int position) {
            final ShopTypeBean model = mItems.get(position);
            if (!TextUtils.isEmpty(model.getName())) {
                tv_title.setText(model.getName());
                tv_title.setSelected(model.isCheck());
            }
            tv_color.setVisibility(model.isCheck() ? View.VISIBLE : View.GONE);
            itemView.setBackgroundColor(model.isCheck() ? 0xffffffff : 0x00000000);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < mItems.size(); i++) {
                        mItems.get(i).setCheck(false);
                    }
                    model.setCheck(true);
                    if (onClickListener!=null){
                        itemView.setTag(model.getId());
                        onClickListener.onClick(itemView);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
