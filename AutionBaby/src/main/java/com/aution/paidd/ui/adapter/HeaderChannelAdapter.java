package com.aution.paidd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aution.paidd.R;
import com.aution.paidd.model.ChannelEntity;

import java.util.List;

import me.xiaopan.sketch.SketchImageView;


/**
 * Created by sunfusheng on 16/4/20.
 */
public class HeaderChannelAdapter extends BaseAdapter {


    List<ChannelEntity> list;
    Context context;

    int textSize;


    public HeaderChannelAdapter(Context context, List<ChannelEntity> list) {
        this.list = list;
        this.context=context;
    }

    public HeaderChannelAdapter(Context context, List<ChannelEntity> list, int textSize) {
        this.list = list;
        this.textSize=textSize;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ChannelEntity getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setTextSize(int size){
        textSize=size;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_channel, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChannelEntity entity = getItem(position);
        holder.tvTitle.setText(entity.getTitle());

        if (textSize!=0){
            holder.tvTitle.setTextSize(textSize);
        }

        if (!TextUtils.isEmpty(entity.getImg())){
            holder.ivImage.displayImage(entity.getImg());
            //Glide.with(context).load(entity.getImg()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.ivImage);
        }else {
            //Glide.with(context).load(entity.getResid()).into(holder.ivImage);
            holder.ivImage.displayResourceImage(entity.getResid());
        }
        return convertView;
    }

    static class ViewHolder {
        SketchImageView ivImage;
        TextView tvTitle;

        ViewHolder(View view) {
            ivImage=(SketchImageView)view.findViewById(R.id.iv_image);
            tvTitle=(TextView)view.findViewById(R.id.tv_title);
        }
    }
}
