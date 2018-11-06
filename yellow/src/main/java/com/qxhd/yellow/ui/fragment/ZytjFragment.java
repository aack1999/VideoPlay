package com.qxhd.yellow.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.model.BannerBean;
import com.framework.core.widget.pull.BaseViewHolder;
import com.qxhd.yellow.R;
import com.qxhd.yellow.model.ChannelEntity;
import com.qxhd.yellow.model.MuiltType;
import com.qxhd.yellow.ui.adapter.HeaderChannelAdapter;
import com.qxhd.yellow.widget.FixedGridView;
import com.youth.banner.Banner;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.process.RoundRectImageProcessor;

public class ZytjFragment extends BaseListFragment<MuiltType> {



    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getActivity().getLayoutInflater().inflate(R.layout.fragment_zytj_layout,parent,false));
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.framework_base_recyclerview;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public void initValue() {
        mDataList.add(new MuiltType().setItem_type(1));
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    public void onRefresh(int action) {

    }

    class MyViewHolder extends BaseViewHolder{

        @BindView(R.id.fix_gridview)
        FixedGridView fix_gridview;


        List<BannerBean> mDataItem;


        @BindView(R.id.banner)
        MZBannerView mMZBanner;

        @BindView(R.id.img1)
        SketchImageView img1;
        @BindView(R.id.img2)
        SketchImageView img2;
        @BindView(R.id.img3)
        SketchImageView img3;
        @BindView(R.id.img4)
        SketchImageView img4;
        @BindView(R.id.img5)
        SketchImageView img5;
        @BindView(R.id.img6)
        SketchImageView img6;


        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            img1.getOptions().setImageProcessor(new RoundRectImageProcessor(8));
            img1.setScaleType(ImageView.ScaleType.FIT_XY);
            img1.displayResourceImage(R.drawable.login_bg);

            img2.getOptions().setImageProcessor(new RoundRectImageProcessor(8));
            img2.setScaleType(ImageView.ScaleType.FIT_XY);
            img2.displayResourceImage(R.drawable.login_bg);

            img3.getOptions().setImageProcessor(new RoundRectImageProcessor(8));
            img3.setScaleType(ImageView.ScaleType.FIT_XY);
            img3.displayResourceImage(R.drawable.login_bg);

            img4.getOptions().setImageProcessor(new RoundRectImageProcessor(8));
            img4.setScaleType(ImageView.ScaleType.FIT_XY);
            img4.displayResourceImage(R.drawable.login_bg);

            img5.getOptions().setImageProcessor(new RoundRectImageProcessor(8));
            img5.setScaleType(ImageView.ScaleType.FIT_XY);
            img5.displayResourceImage(R.drawable.login_bg);

            img6.getOptions().setImageProcessor(new RoundRectImageProcessor(8));
            img6.setScaleType(ImageView.ScaleType.FIT_XY);
            img6.displayResourceImage(R.drawable.login_bg);

            List items = new ArrayList();
            for (int i = 0; i < 5; i++) {
                items.add(null);
            }
            // 设置数据
            mMZBanner.setPages(items, new MZHolderCreator<HomeFragment.BannerViewHolder>() {
                @Override
                public HomeFragment.BannerViewHolder createViewHolder() {
                    return new HomeFragment.BannerViewHolder();
                }
            });

            List<ChannelEntity> list = new ArrayList<>();
            list.add(new ChannelEntity("无码", R.drawable.all_classify));
            list.add(new ChannelEntity("各行各业", R.drawable.all_classify));
            list.add(new ChannelEntity("辣妹大奶", R.drawable.all_classify));
            list.add(new ChannelEntity("角色扮演", R.drawable.all_classify));
            list.add(new ChannelEntity("制服诱惑", R.drawable.all_classify));
            list.add(new ChannelEntity("人妻熟女", R.drawable.all_classify));
            list.add(new ChannelEntity("美少女", R.drawable.all_classify));
            list.add(new ChannelEntity("全部", R.drawable.all_classify));
            fix_gridview.setAdapter(new HeaderChannelAdapter(getContext(), list));
            fix_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }


    public static ZytjFragment newFragmet(String title){
        ZytjFragment f=new ZytjFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        f.setArguments(bundle);
        return f;
    }

}
