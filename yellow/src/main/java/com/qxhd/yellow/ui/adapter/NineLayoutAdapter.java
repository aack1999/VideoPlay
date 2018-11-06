package com.qxhd.yellow.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qxhd.yellow.R;
import com.qxhd.yellow.model.ChannelEntity;
import com.qxhd.yellow.model.NineLayoutInof;

import java.util.List;

import me.xiaopan.sketch.SketchImageView;


/**
 * Created by sunfusheng on 16/4/20.
 */
public class NineLayoutAdapter extends BaseAdapter {


    List<NineLayoutInof> list;
    Context context;

    int textSize;


    public NineLayoutAdapter(Context context, List<NineLayoutInof> list) {
        this.list = list;
        this.context=context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NineLayoutInof getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nine_layout_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NineLayoutInof entity = getItem(position);
        holder.tvTitle.setText(entity.getTitle());

        if (textSize!=0){
            holder.tvTitle.setTextSize(textSize);
        }

//        if (!TextUtils.isEmpty(entity.getImg())){
//            holder.ivImage.displayImage(entity.getImg());
//        }else {
//            holder.ivImage.displayImage(entity.getImg());
//        }
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
