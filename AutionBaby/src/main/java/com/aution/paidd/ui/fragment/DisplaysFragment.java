package com.aution.paidd.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.PullRecycler;
import com.ninegrid.ImageInfo;
import com.ninegrid.NineGridView;
import com.ninegrid.preview.NineGridViewClickAdapter;
import com.aution.paidd.R;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.LuckyShowListBean;
import com.aution.paidd.request.LuckyShowRequest;
import com.aution.paidd.response.LuckyShowListResponse;


import java.util.ArrayList;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import rx.Subscriber;


/**
 * 晒单列表
 * Created by quxiang on 2016/12/26.
 */
public class DisplaysFragment extends BaseListFragment<LuckyShowListBean> {

    String imgBaseUrl;
    String aid;

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_lucky_show;
    }


    @Override
    public void initValue() {
        aid=getActivity().getIntent().getStringExtra("aid");
        getData(true);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.fragment_lucky_item, parent, false));
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            currentPage = 1;
        }
        getData(false);
    }

    public void getData(boolean isShow) {
        if (isShow){
            currentPage=1;
        }
        toggleShowLoading(isShow,null);
        LuckyShowRequest request=new LuckyShowRequest();
        request.setCurrentpage(currentPage+"");
        request.setMaxresult(rows+"");
        request.setUid(getActivity().getIntent().getStringExtra("uid"));

        Subscriber<LuckyShowListResponse> subscriber=new Subscriber<LuckyShowListResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                recycler.onRefreshCompleted();
                toggleShowEmpty(true, Constant.Service_Err, "重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData(true);
                    }
                });
            }

            @Override
            public void onNext(LuckyShowListResponse popuBean) {
                recycler.onRefreshCompleted();
                toggleShowLoading(false, null);
                if (popuBean != null) {
                    if (popuBean.getCode() == 10000) {
                        imgBaseUrl = popuBean.getMsg();
                        if (currentPage == 1) {
                            mDataList.clear();
                        }
                        mDataList.addAll(popuBean.getObj().getResultlist());
                        totalPage=popuBean.getObj().getTotalrecord();
                        if (popuBean.getObj().getResultlist().size()<=0){
                            recycler.enableLoadMore(false);
                        }else {
                            recycler.enableLoadMore(true);
                        }
                        currentPage++;
                        if (mDataList.size() <= 0) {
                            toggleShowEmpty(true, "暂无晒单数据", "刷新", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getData(true);
                                }
                            });
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        toggleShowEmpty(true, popuBean.getMsg(), "刷新", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getData(true);
                            }
                        });
                    }

                } else {
                    toggleShowEmpty(true, "网络异常,请刷新", "刷新", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getData(true);
                        }
                    });
                }
            }
        };
        HttpMethod.getInstance().getShareList(subscriber,request);
    }

    class MyViewHolder extends BaseViewHolder{

        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.ninegridview)
        NineGridView ninegridview;
        @BindView(R.id.img_logo)
        SketchImageView img_logo;
        @BindView(R.id.tv_nameid)
        TextView tv_nameid;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {
            DisplayOptions options = new DisplayOptions();
            options.setImageShaper(new CircleImageShaper());
            options.setLoadingImage(R.drawable.userpic);
            options.setErrorImage(R.drawable.userpic);
            img_logo.setOptions(options);
            img_logo.displayImage(mDataList.get(position).getImg());

            tv_time.setText(mDataList.get(position).getCreatetime());
            tv_content.setText(mDataList.get(position).getContent());
            tv_title.setText(mDataList.get(position).getTitle());
            tv_nameid.setText(mDataList.get(position).getNickname());

            String img=mDataList.get(position).getImages();
            if(!TextUtils.isEmpty(img)){
                ninegridview.setVisibility(View.VISIBLE);
                final String[] newArryString = mDataList.get(position).getImages().split(";");
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < newArryString.length; i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(imgBaseUrl+newArryString[i]);
                    info.setBigImageUrl(imgBaseUrl+newArryString[i]);
                    imageInfo.add(info);
                }
                ninegridview.setAdapter(new NineGridViewClickAdapter(getContext(), imageInfo));
            }else {
                ninegridview.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

}
