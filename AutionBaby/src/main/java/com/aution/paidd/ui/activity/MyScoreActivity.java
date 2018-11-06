package com.aution.paidd.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.aution.paidd.R;
import com.aution.paidd.bean.CoinIncomeBean;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.MD5Util;
import com.aution.paidd.request.CoinIncomeRequest;
import com.aution.paidd.request.ExcahngeRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.CoinIncomeResponse;
import com.aution.paidd.ui.widget.InnerMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * 我的积分
 * Created by quxiang on 2017/9/2.
 */

public class MyScoreActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.listview)
    InnerMoreListView listView;

    TextView tv_shop_coin;

    List<CoinIncomeBean> mDataList;
    MyAdapter adapter;
    UserBean model;
    int moneyCount = 100;

    View pay_center_5, pay_center_10, pay_center_20, pay_center_50, pay_center_100, pay_center_200;
    TextView tv_count;
    View layout_symx, layout_zcmx;

    int state = 1;
    int currentPage = 1;
    int rows = 20;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_myscore;
    }

    @Override
    public void initValue() {
        model = CacheData.getInstance().getUserData();
        setTitle("我的积分");

        initHeaderView();

        mDataList = new ArrayList();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url", HttpMethod.BASE_URL + "exchange");
                doActivity(H5Activity.class, bundle);
            }
        });
    }

    public void initHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.list_score_head_item, null);
        listView.addHeaderView(view);
        SketchImageView img_gb = getView(view, R.id.img_gb);
        TextView title=getView(view,R.id.temp);
        title.setText("积分合计");
        img_gb.displayResourceImage(R.drawable.ic_jfhj_bg);
        tv_shop_coin = getView(view, R.id.tv_shop_coin);
        tv_shop_coin.setText(model.getExperience() + "");

        pay_center_5 = view.findViewById(R.id.pay_center_5);
        pay_center_10 = view.findViewById(R.id.pay_center_10);
        pay_center_20 = view.findViewById(R.id.pay_center_20);
        pay_center_50 = view.findViewById(R.id.pay_center_50);
        pay_center_100 = view.findViewById(R.id.pay_center_100);
        pay_center_200 = view.findViewById(R.id.pay_center_200);
        pay_center_5.setOnClickListener(this);
        pay_center_10.setOnClickListener(this);
        pay_center_20.setOnClickListener(this);
        pay_center_50.setOnClickListener(this);
        pay_center_100.setOnClickListener(this);
        pay_center_200.setOnClickListener(this);

        tv_count = (TextView) view.findViewById(R.id.tv_count);

        layout_symx = view.findViewById(R.id.btn_cyjl);
        layout_zcmx = view.findViewById(R.id.btn_lshdz);

        layout_symx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab(0);
            }
        });

        layout_zcmx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab(1);
            }
        });


        selectTab(0);

        onClick(view.findViewById(R.id.pay_center_5));
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        listView.setPtrLoadListener(new InnerMoreListView.PtrLoadListener() {
            @Override
            public void onLoadMore() {
                getData();
            }
        });


    }


    public void selectTab(int index) {
        if (index == 0) {
            state = 1;
        } else {
            state = 2;
        }

        layout_symx.findViewById(R.id.tv_title_cyjl).setSelected(index == 0);
        layout_symx.findViewById(R.id.line_cyjl).setVisibility(index == 0 ? View.VISIBLE : View.INVISIBLE);
        layout_zcmx.findViewById(R.id.tv_title_llhdz).setSelected(index == 1);
        layout_zcmx.findViewById(R.id.line_llhdz).setVisibility(index == 1 ? View.VISIBLE : View.INVISIBLE);

        currentPage = 1;
        getData();
    }

    @Override
    public void onClick(View view) {
        pay_center_5.setSelected(false);
        pay_center_10.setSelected(false);
        pay_center_20.setSelected(false);
        pay_center_50.setSelected(false);
        pay_center_100.setSelected(false);
        pay_center_200.setSelected(false);
        switch (view.getId()) {
            case R.id.pay_center_5:
                moneyCount = 100;
                break;
            case R.id.pay_center_10:
                moneyCount = 1000;
                break;
            case R.id.pay_center_20:
                moneyCount = 3000;
                break;
            case R.id.pay_center_50:
                moneyCount = 5000;
                break;
            case R.id.pay_center_100:
                moneyCount = 10000;
                break;
            case R.id.pay_center_200:
                moneyCount = 100000;
                break;
        }
        view.setSelected(true);
        tv_count.setText(moneyCount / 100 + "赠币");
    }

    public void getData() {
        Subscriber<CoinIncomeResponse> subscriber = new Subscriber<CoinIncomeResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CoinIncomeResponse response) {
                listView.onCompleRefresh();
                if (response != null) {
                    if (response.getCode() == 10000) {

                        if (response.getObj() != null && response.getObj().getResultlist() != null) {
                            if (currentPage == 1) {
                                mDataList.clear();
                            }
                            mDataList.addAll(response.getObj().getResultlist());
                            if (response.getObj().getResultlist().size() <= 0) {
                                listView.setEnbleLoadMore(false);
                            } else {
                                listView.setEnbleLoadMore(true);
                            }

                            currentPage++;

                            adapter.notifyDataSetChanged();
                        }
                    } else {

                    }
                } else {
                    listView.setEnbleLoadMore(false);
                }
            }
        };
        CoinIncomeRequest request = new CoinIncomeRequest();
        request.setUid(CacheData.getInstance().getUserData().getUid());
        request.setState(state + "");
        request.setCurrentpage(currentPage + "");
        request.setMaxresult(rows + "");
        HttpMethod.getInstance().getExperienceList(subscriber, request);
    }

    public void submit() {
        Subscriber<BaseResponse> subscriber = new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                if (baseResponse != null && baseResponse.getCode() == 10000) {
                    //兑换成功
                    showToast("兑换成功");
                    UserBean userBean = CacheData.getInstance().getUserData();
                    userBean.setExperience(userBean.getExperience() - moneyCount);
                    CacheData.getInstance().cacheUserData(userBean);
                    tv_shop_coin.setText(userBean.getExperience() + "");
                    if (state == 2) {
                        selectTab(1);
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        };
        ExcahngeRequest request = new ExcahngeRequest();
        request.setUid(CacheData.getInstance().getUserData().getUid());
        request.setNumber(moneyCount + "");
        String pp = "number=" + request.getNumber() + "&uid=" + request.getUid() + "&secret=" + Constant.ExchangeKey;
        request.setSign(MD5Util.encode(pp).toUpperCase());

        HttpMethod.getInstance().getExchange(subscriber, request);
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewholder holder;
            if (view == null) {

                view = getLayoutInflater().inflate(R.layout.item_exchange_detail, null);
                holder = new MyViewholder(view);
                view.setTag(holder);
            } else {
                holder = (MyViewholder) view.getTag();
            }

            holder.onBindData(i);

            return view;
        }


    }

    class MyViewholder {

        TextView tv_time;
        TextView tv_content;
        TextView tv_count;

        public MyViewholder(View view) {
            tv_time = getView(view, R.id.tv_time);
            tv_content = getView(view, R.id.tv_content);
            tv_count = getView(view, R.id.tv_count);
        }

        public void onBindData(int position) {
            tv_time.setText(mDataList.get(position).getCreatetime().replaceAll(" ", "\n"));
            tv_content.setText(mDataList.get(position).getRemark());
            tv_count.setText((state == 1 ? "+" : "-") + mDataList.get(position).getNumber());
        }
    }

}
