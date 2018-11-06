package com.qxhd.yellow.ui.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.imp.GlideImageLoader;
import com.framework.core.widget.pull.BaseViewHolder;
import com.qxhd.yellow.R;
import com.qxhd.yellow.model.ChannelEntity;
import com.qxhd.yellow.model.MuiltType;
import com.qxhd.yellow.model.NineLayoutInof;
import com.qxhd.yellow.ui.activity.ChannelActivity;
import com.qxhd.yellow.ui.activity.PlayVideoActivity;
import com.qxhd.yellow.ui.adapter.HeaderChannelAdapter;
import com.qxhd.yellow.ui.adapter.NineLayoutAdapter;
import com.qxhd.yellow.widget.FixedGridView;
import com.qxhd.yellow.widget.NineLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindInt;
import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;

/**
 * Created by quxiang on 2017/8/29.
 */

public class HomeFragment extends BaseListFragment<MuiltType> {


    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initValue() {
        initData();
    }

    @Override
    protected void onFirstUserVisible() {

    }

    public void initData() {
        mDataList.add(new MuiltType().setItem_type(1));
        mDataList.add(new MuiltType().setItem_type(2));
        mDataList.add(new MuiltType().setItem_type(2));
        mDataList.add(new MuiltType().setItem_type(2));
        mDataList.add(new MuiltType().setItem_type(2));
        mDataList.add(new MuiltType().setItem_type(2));
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ViewHolderBanner(getActivity().getLayoutInflater().inflate(R.layout.viewholder_home_banner, parent, false));
            case 2:
                return new ViewHolderItem(getActivity().getLayoutInflater().inflate(R.layout.viewholder_home_item_6, parent, false));
        }
        return null;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public int getItemType(int position) {
        return mDataList.get(position).getItem_type();
    }

    @Override
    public void onRefresh(int action) {

    }

    class ViewHolderBanner extends BaseViewHolder {


        @BindView(R.id.banner)
        MZBannerView mMZBanner;

        public ViewHolderBanner(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

            List items = new ArrayList();
            for (int i = 0; i < 5; i++) {
                items.add(null);
            }
            // 设置数据
            mMZBanner.setPages(items, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });

        }


        @Override
        public void onItemClick(View view, int position) {

        }
    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private SketchImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (SketchImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            // 数据绑定
            //mImageView.displayImage(data);
        }
    }


    class ViewHolderItem extends BaseViewHolder {

        @BindView(R.id.fix_gridview)
        FixedGridView fix_gridview;

        public ViewHolderItem(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doActivity(ChannelActivity.class);
                }
            });
        }

        @Override
        public void onBindViewHolder(int position) {
            List<NineLayoutInof> data = new ArrayList<>();
            data.add(new NineLayoutInof("https://c3d9.vvvvbaidu.com/xx/watermark/5437078/tmp0v03xgdh.gif", "test"));
            data.add(new NineLayoutInof("https://c3d9.vvvvbaidu.com/xx/watermark/5437078/tmp0v03xgdh.gif", "test"));
            data.add(new NineLayoutInof("https://c3d9.vvvvbaidu.com/xx/watermark/5437078/tmp0v03xgdh.gif", "test"));
            data.add(new NineLayoutInof("https://c3d9.vvvvbaidu.com/xx/watermark/5437078/tmp0v03xgdh.gif", "test"));
            data.add(new NineLayoutInof("https://c3d9.vvvvbaidu.com/xx/watermark/5437078/tmp0v03xgdh.gif", "test"));
            data.add(new NineLayoutInof("https://c3d9.vvvvbaidu.com/xx/watermark/5437078/tmp0v03xgdh.gif", "test"));
            fix_gridview.setNumColumns(2);
            fix_gridview.setAdapter(new NineLayoutAdapter(getActivity(),data));

            fix_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    doActivity(PlayVideoActivity.class);
                }
            });

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
