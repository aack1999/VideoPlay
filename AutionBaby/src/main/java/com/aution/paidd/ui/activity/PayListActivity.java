package com.aution.paidd.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.core.base.ActivityManager;
import com.framework.core.base.BaseListActivity;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.PullRecycler;
import com.aution.paidd.R;
import com.aution.paidd.bean.PayListItemBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.response.PayListResponse;
import com.aution.paidd.utils.DateUtils;


import butterknife.BindView;
import rx.Subscriber;


/**
 * Created by quxiang on 2016/12/12.
 */
public class PayListActivity extends BaseListActivity<PayListItemBean> {


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        setTitle("充值记录");
        getData(true);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.activity_pay_list_item, parent, false));
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
        toggleShowLoading(isShow, null);

        BaseIdRequest request=new BaseIdRequest();
        request.setUid(CacheData.getInstance().getUserData().getUid());
        request.setCurrentpage(currentPage+"");
        request.setMaxresult(rows+"");
        Subscriber<PayListResponse> subscriber=new Subscriber<PayListResponse>() {
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
            public void onNext(PayListResponse exChangeResponse) {
                recycler.onRefreshCompleted();
                toggleShowLoading(false, null);
                if (exChangeResponse != null) {
                    if (exChangeResponse.getCode() == 10000 && exChangeResponse.getObj() != null) {
                        if (currentPage == 1) {
                            mDataList.clear();
                        }
                        mDataList.addAll(exChangeResponse.getObj().getResultlist());
                        totalPage=exChangeResponse.getObj().getTotalrecord();
                        if (mDataList.size()>=totalPage){
                            recycler.enableLoadMore(false);
                        }else {
                            recycler.enableLoadMore(true);
                        }
                        currentPage++;
                        if (mDataList.size() <= 0) {
                            toggleShowEmpty(true, "暂无记录", "立即充值", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goToHome();
                                }
                            });
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        toggleShowEmpty(true, "暂无记录", "立即充值", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goToHome();
                            }
                        });
                    }
            }
        }
        };
        HttpMethod.getInstance().getPayLogList(subscriber,request);
    }

    class MyViewHolder extends BaseViewHolder {

        @BindView(R.id.pay_list_item_money)
        TextView pay_list_item_money;
        @BindView(R.id.pay_list_item_time)
        TextView pay_list_item_time;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PayListItemBean bean = mDataList.get(position);
            pay_list_item_money.setText("充值金额：￥"+bean.getAmount()+",折算为"+((int)Float.parseFloat(bean.getAmount()))+"拍币");
            pay_list_item_time.setText(DateUtils.stringTostring(bean.getOrdertime(),"yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm"));
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    public void goToHome(){
        ActivityManager.getInstance().finishAllActivityEx(MainActivity.class);
        doActivity(PayCenterActivity.class);
    }
}
