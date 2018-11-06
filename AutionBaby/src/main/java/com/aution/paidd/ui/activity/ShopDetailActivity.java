package com.aution.paidd.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.core.base.BaseListActivity;
import com.framework.core.imp.GlideImageLoader;
import com.framework.core.model.BannerBean;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.utils.LogUtils;
import com.framework.core.utils.ParseUtils;
import com.framework.core.utils.TextSpanUtils;
import com.framework.core.widget.FlatButton;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.layoutmanager.ILayoutManager;
import com.framework.core.widget.pull.layoutmanager.MyLinearLayoutManager;
import com.ninegrid.ImageInfo;
import com.ninegrid.NineGridView;
import com.ninegrid.preview.NineGridViewClickAdapter;
import com.aution.paidd.R;
import com.aution.paidd.bean.GoodsBuyBean;
import com.aution.paidd.bean.LrecordBean;
import com.aution.paidd.bean.ServceMessageBean;
import com.aution.paidd.bean.ShopDetialItem;
import com.aution.paidd.bean.ShopHistoryBean;
import com.aution.paidd.bean.ShopLbidBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.OnShareCallBack;
import com.aution.paidd.model.MuiltType;
import com.aution.paidd.request.GoodsBuyRequest;
import com.aution.paidd.request.LuckyShowListBean;
import com.aution.paidd.request.LuckyShowRequest;
import com.aution.paidd.request.ShopDetailRequest;
import com.aution.paidd.request.ShopHistoryRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.LuckyShowListResponse;
import com.aution.paidd.response.ShopDetialResponse;
import com.aution.paidd.response.ShopHistoryResponse;
import com.aution.paidd.response.UserMoneyResponse;
import com.aution.paidd.ui.widget.ShopNumberView;
import com.aution.paidd.utils.FormatUtils;
import com.aution.paidd.utils.ShareUtils;
import com.switfpass.pay.utils.MD5;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.apache.commons.codec.binary.Base64;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/9/2.
 */

public class ShopDetailActivity extends BaseListActivity<MuiltType> {

    List<BannerBean> mDataItem;

    public ShopDetialItem model;

    @BindView(R.id.suspension_bar)
    LinearLayout mSuspensionBar;

    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_nowprice)
    TextView tv_nowprice;

    @BindView(R.id.btn_su)
    TextView btn_su;
    @BindView(R.id.btn_add)
    TextView btn_add;
    int count = 1;
    @BindView(R.id.tv_join_count)
    TextView tv_join_count;
    @BindView(R.id.btn_buy)
    FlatButton btn_buy;
    @BindView(R.id.btn_next)
    FlatButton btn_next;

    @BindView(R.id.tv_money_count)
    TextView tv_money_count;

    MyLinearLayoutManager linearLayoutManager;

    View tab_item;//tab切换的布局

    TextView tv_time_count;

    String imgBaseUrl;


    TextView tv_buy_count;
    int paicount;
    int givecount;

    int outcount;//出价的钱

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        initData();
        setTitle("竞拍详情");
        mDataItem = new ArrayList<>();
        recycler.enablePullToRefresh(false);
        upScrollListener();

        ShopDetialResponse bean = (ShopDetialResponse) getIntent().getSerializableExtra("model");
        if (bean != null) {
            upShopDetailUI(bean, false);
        } else {
            getData(false, false);
        }
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model != null) {
                    String kk[] = model.getInfo().getImges().split(";");
                    ShareUtils.showAllShare(ShopDetailActivity.this, "拍我喜欢，竟在其中", null, model.getInfo().getName(), imgBaseUrl + kk[0], 0, new OnShareCallBack() {
                        @Override
                        public void onSuccess() {
                            showToast("分享成功");
                        }
                    });
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count > 5000) {
                    count = 5000;
                }
                tv_join_count.setText(count + "");
            }
        });

        btn_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (--count <= 0) {
                    count = 1;
                }
                tv_join_count.setText(count + "");
            }
        });

        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!CacheData.getInstance().isLogin()) {
                    goToLogin();
                    return;
                }

                submitBuy();
            }
        });

        btn_buy.setTextSize(12);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getData(false, true);
            }
        });

        findViewById(R.id.tv_join_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditShopNumberDialog();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BroadcastAction);
        registerReceiver(myReceiver, filter);
    }


    public void initData() {

    }

    public void upScrollListener() {
        recycler.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取第一个可见view的位置
                int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstItemPosition >= 1) {
                    if (mSuspensionBar.getVisibility() != View.VISIBLE)
                        mSuspensionBar.setVisibility(View.VISIBLE);
                } else {
                    if (mSuspensionBar.getVisibility() != View.GONE)
                        mSuspensionBar.setVisibility(View.GONE);
                }

            }
        });
    }

    public void getData(boolean isShow, final boolean isNext) {
        if (isShow) {
            tabIndex = 0;
        }
        toggleShowLoading(isShow, null);

        if (isNext) {
            showDialog(null);
        }

        Subscriber<ShopDetialResponse> subscriber = new Subscriber<ShopDetialResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                closeDialog();
            }

            @Override
            public void onNext(ShopDetialResponse baseResponse) {
                closeDialog();
                upShopDetailUI(baseResponse, isNext);
            }
        };
        ShopDetailRequest request = new ShopDetailRequest();
        request.setId(getIntent().getStringExtra("id"));
        request.setView(getIntent().getStringExtra("view"));
        request.setUid(CacheData.getInstance().getUserData().getUid());

        if (isNext) {
            request.setId(model.getInfo().getCid());
            request.setView("2");
        }

        HttpMethod.getInstance().getGoodsDetail(subscriber, request);
    }

    public void upShopDetailUI(ShopDetialResponse baseResponse, boolean isNext) {
        if (baseResponse != null) {
            if (baseResponse.getCode() == 10000) {

                if (isNext) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", baseResponse);
                    bundle.putString("id", model.getInfo().getCid());
                    bundle.putString("view", "1");
                    doActivity(ShopDetailActivity.class, bundle);
                    return;
                }

                if (baseResponse.getObj() != null) {
                    model = baseResponse.getObj();
                    if (baseResponse.getObj().getInfo() != null && !TextUtils.isEmpty(baseResponse.getObj().getInfo().getImges())) {
                        String kk[] = baseResponse.getObj().getInfo().getImges().split(";");
                        for (int i = 0; i < kk.length; i++) {
                            mDataItem.add(new BannerBean(baseResponse.getMsg() + kk[i]));
                        }
                    }

                    if (model.getInfo().getStatus() == 1) {
                        findViewById(R.id.layout_buy).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_next).setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.layout_buy).setVisibility(View.GONE);
                        findViewById(R.id.btn_next).setVisibility(View.VISIBLE);
                    }


                    paicount = model.getInfo().getPaicount();
                    givecount = model.getInfo().getGivecount();

                    mDataList.clear();
                    mDataList.add(new MuiltType().setItem_type(1));
                    mDataList.add(new MuiltType().setItem_type(2));
                    mDataList.add(new MuiltType().setItem_type(3));
                    mDataList.add(new MuiltType().setItem_type(4));
                    upPriceLayoutData();
                    getLuckyShowData();

                    startRefreshTime(model.getInfo().getCountdown());

                    adapter.notifyDataSetChanged();
                }
            } else {

                if (isNext) {
                    showToast(baseResponse.getMsg());
                    return;
                }

                toggleShowEmpty(true, baseResponse.getMsg(), "刷新", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData(true, false);
                    }
                });
            }
        } else {
            toggleShowEmpty(true, Constant.Service_Err, "刷新", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getData(true, false);
                }
            });
        }
    }

    public void getUserMoney() {
        Subscriber<UserMoneyResponse> subscriber = new Subscriber<UserMoneyResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserMoneyResponse userMoneyResponse) {
                if (userMoneyResponse != null && userMoneyResponse.getCode() == 10000 && userMoneyResponse.getObj() != null) {
                    paicount = userMoneyResponse.getObj().getPaicount();
                    givecount = userMoneyResponse.getObj().getGivecount();
                    tv_buy_count.setText("累计消费：" + paicount + "拍币，" + givecount + "赠币");
                    tv_money_count.setText(paicount + givecount + "/" + (model.getInfo().getCount() + outcount));
                }
            }
        };
        ShopDetailRequest request = new ShopDetailRequest();
        request.setUid(CacheData.getInstance().getUserData().getUid());
        request.setAid(model.getInfo().getAid());
        HttpMethod.getInstance().getUserMoney(subscriber, request);
    }

    public void startRefreshTime(int time) {
        setTime(1000*time,10);
    }

    public void getHistoryData() {

        if (currentPage == 1) {
            showDialog("正在加载数据");
        }

        Subscriber<ShopHistoryResponse> subscriber = new Subscriber<ShopHistoryResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                closeDialog();
                toggleShowLoading(false, null);
                recycler.onRefreshCompleted();
            }

            @Override
            public void onNext(ShopHistoryResponse shopHistoryResponse) {
                closeDialog();
                toggleShowLoading(false, null);
                recycler.onRefreshCompleted();
                if (shopHistoryResponse != null && shopHistoryResponse.getCode() == 10000 && shopHistoryResponse.getObj() != null && shopHistoryResponse.getObj().getResultlist() != null) {
                    if (currentPage == 1) {
                        mDataList.clear();
                        for (int i = 1; i <= 4; i++) {
                            mDataList.add(new MuiltType().setItem_type(i));
                        }
                    }
                    mDataList.addAll(shopHistoryResponse.getObj().getResultlist());
                    currentPage++;
                    if (shopHistoryResponse.getObj().getResultlist().size() <= 0) {
                        recycler.enableLoadMore(false);
                    } else {
                        recycler.enableLoadMore(true);
                    }
                } else {
                    recycler.enableLoadMore(false);
                }

                if (mDataList.size() < 5) {
                    //没有数据
                    mDataList.add(new MuiltType().setItem_type(6));
                }

                adapter.notifyItemRangeChanged(5, mDataList.size());
            }
        };
        ShopHistoryRequest request = new ShopHistoryRequest();
        request.setCurrentpage(currentPage + "");
        request.setCid(model.getInfo().getCid());
        request.setMaxresult(rows + "");
        HttpMethod.getInstance().getGoodHistoryList(subscriber, request);
    }

    public void getLuckyShowData() {

        if (currentPage == 1) {
            showDialog("正在加载数据");
        }

        LuckyShowRequest request = new LuckyShowRequest();
        request.setCurrentpage(currentPage + "");
        request.setMaxresult(rows + "");
        request.setAid(model.getInfo().getAid());

        Subscriber<LuckyShowListResponse> subscriber = new Subscriber<LuckyShowListResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                closeDialog();
                recycler.onRefreshCompleted();
                toggleShowEmpty(true, Constant.Service_Err, "重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData(true, false);
                    }
                });
            }

            @Override
            public void onNext(LuckyShowListResponse popuBean) {
                closeDialog();
                recycler.onRefreshCompleted();
                toggleShowLoading(false, null);
                if (popuBean != null) {
                    if (popuBean.getCode() == 10000) {
                        imgBaseUrl = popuBean.getMsg();
                        if (currentPage == 1) {
                            mDataList.clear();
                            for (int i = 1; i <= 4; i++) {
                                mDataList.add(new MuiltType().setItem_type(i));
                            }
                        }
                        mDataList.addAll(popuBean.getObj().getResultlist());
                        if (popuBean.getObj().getResultlist().size() <= 0) {
                            recycler.enableLoadMore(false);
                            if (mDataList.size() < 5) {
                                //没有数据
                                mDataList.add(new MuiltType().setItem_type(6));
                            } else {
                                //没有更多了
                                mDataList.add(new MuiltType().setItem_type(7));
                            }
                        } else {
                            recycler.enableLoadMore(true);
                        }
                        currentPage++;
                    } else {
                        recycler.enableLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };
        HttpMethod.getInstance().getShareList(subscriber, request);
    }

    public void upPriceLayoutData() {
        tv_price.setText("￥" + model.getInfo().getPrice());
        tv_nowprice.setText("￥" + model.getInfo().getNowprice());
//        tv_count_down.setText(FormatUtils.formatCountDown(model.getInfo().getCountdown()));
//        if (model.getInfo().getStatus() != 1) {
//            tv_count_down.setVisibility(View.GONE);
//            mSuspensionBar.findViewById(R.id.temp).setVisibility(View.GONE);
//            findViewById(R.id.temp).setVisibility(View.GONE);
//        } else {
//            tv_count_down.setVisibility(View.VISIBLE);
//            mSuspensionBar.findViewById(R.id.temp).setVisibility(View.VISIBLE);
//            findViewById(R.id.layout_price_bg).setBackgroundColor(getResources().getColor(R.color.yellow));
//            findViewById(R.id.temp).setVisibility(View.VISIBLE);
//
//        }
    }


    public void submitBuy() {
        showDialog("正在提交数据");
        Subscriber<BaseResponse> subscriber = new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                closeDialog();
                showToast(Constant.Service_Err);
            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                closeDialog();
                if (baseResponse != null) {
                    showToast(baseResponse.getMsg());

                    if (baseResponse.getCode() == 10000) {
                        outcount += count;
                        count = 1;
                        tv_join_count.setText(count + "");
                    }

                    if (baseResponse.getCode() == 20000) {
                        doActivity(PayCenterActivity.class);
                        return;
                    }

                }
            }
        };


        GoodsBuyRequest request = new GoodsBuyRequest();
        GoodsBuyBean gbb = new GoodsBuyBean();
        gbb.setAid(Integer.parseInt(model.getInfo().getAid()));
        gbb.setCount(count);
        String sign = "aid=" + gbb.getAid() + "&count=" + gbb.getCount() + "&uid=" + gbb.getUid() + "&secret=" + Constant.GoodsBuyKey;
        request.setSign(MD5.md5s(sign).toUpperCase());
        request.setTobuyinfo(new String(Base64.encodeBase64(ParseUtils.toJson(gbb).getBytes())));
        HttpMethod.getInstance().getGoodsBuy(subscriber, request);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 1:
                return new BannerViewHolder(getLayoutInflater().inflate(R.layout.viewholder_detial_banner, parent, false));
            case 2:
                return new PriceViewHolder(getLayoutInflater().inflate(R.layout.viewholder_detail_price, parent, false));
            case 3:
                return new StatementViewHolder(getLayoutInflater().inflate(R.layout.viewholder_detail_statement, parent, false));
            case 4:
                return new TabViewHolder(getLayoutInflater().inflate(R.layout.viewholder_detail_tab, parent, false));
            case 5:
                return new RuleViewHolder(getLayoutInflater().inflate(R.layout.viewholder_detail_rule, parent, false));
            case 6:
                return new NoDataViewHolder(getLayoutInflater().inflate(R.layout.viewholder_detail_no_data, parent, false));
            case 7:
                TextView tv = new TextView(this);
                int dp10 = DisplayUtils.dip2px(this, 10);
                tv.setTextColor(getResources().getColor(R.color.base_text_title));
                tv.setTextSize(14f);
                tv.setBackgroundColor(getResources().getColor(R.color.base_app_bg));
                tv.setWidth(DisplayUtils.getScreenW());
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(dp10, dp10, dp10, dp10);
                return new InfoViewHolder(tv);
            case 8:
                return new LuckyShowViewHolder(getLayoutInflater().inflate(R.layout.fragment_lucky_item, parent, false));

        }
        return new HistoryViewHolder(getLayoutInflater().inflate(R.layout.fragment_history_item, parent, false));
    }


    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        linearLayoutManager = new MyLinearLayoutManager(this);
        return linearLayoutManager;
    }

    @Override
    protected int getItemType(int position) {
        return mDataList.get(position).getItem_type();
    }

    @Override
    public void onRefresh(int action) {
        switch (tabIndex) {
            case 1:
                getHistoryData();
                break;
            case 0:
                getLuckyShowData();
                break;
        }

    }


    /**
     * 选择tab
     *
     * @param index
     */
    int tabIndex = 0;//默认选中参与记录

    public void selectTab(int index) {
        if (index == 0) {
            //晒单分享
            if (tabIndex != index) {
                currentPage = 1;
                //刷新
                getLuckyShowData();
            }
        } else if (index == 1) {
            //历史获得者
            if (tabIndex != index) {
                currentPage = 1;
                getHistoryData();
            }
        } else {
            if (tabIndex != index) {
                mDataList.clear();
                for (int i = 1; i <= 5; i++) {
                    mDataList.add(new MuiltType().setItem_type(i));
                }
                recycler.enableLoadMore(false);
                adapter.notifyDataSetChanged();
            }
        }
        if (tab_item != null) {
            tab_item.findViewById(R.id.line_sdfx).setVisibility(index == 0 ? View.VISIBLE : View.INVISIBLE);
            tab_item.findViewById(R.id.tv_title_sdfx).setSelected(index == 0 ? true : false);
            tab_item.findViewById(R.id.line_wqcj).setVisibility(index == 1 ? View.VISIBLE : View.INVISIBLE);
            tab_item.findViewById(R.id.tv_title_wqcj).setSelected(index == 1 ? true : false);
            tab_item.findViewById(R.id.line_jpgz).setVisibility(index == 2 ? View.VISIBLE : View.INVISIBLE);
            tab_item.findViewById(R.id.tv_title_jpgz).setSelected(index == 2 ? true : false);
        }
        tabIndex = index;
    }


    //=========================================================================ViewHodler================================================


    class BannerViewHolder extends BaseViewHolder {

        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.img_ok)
        SketchImageView img_ok;

        public BannerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            banner.setImageLoader(new GlideImageLoader());
            banner.setDelayTime(3000);
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {

                }
            });
            banner.setImages(mDataItem);
            banner.start();

            btn_next.setEnabled(true);
            btn_next.setText("前往下一期");
            btn_next.setTextColor(Color.WHITE);

            if (model.getInfo().getStatus() == 2) {
                img_ok.setVisibility(View.VISIBLE);
                img_ok.displayResourceImage(R.drawable.ic_ycj);
            } else if (model.getInfo().getStatus() == 3) {
                img_ok.setVisibility(View.VISIBLE);
                img_ok.displayResourceImage(R.drawable.ic_yzt);
                btn_next.setEnabled(false);
                btn_next.setText("商品暂停竞拍");
                btn_next.setTextColor(getResources().getColor(R.color.base_text_title));
            } else {
                img_ok.setVisibility(View.GONE);
            }

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class PriceViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_nowprice)
        TextView tv_nowprice;


        public PriceViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBindViewHolder(int position) {
            tv_price.setText("￥" + model.getInfo().getPrice());
            tv_nowprice.setText("￥" + model.getInfo().getNowprice());
        }


        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class StatementViewHolder extends BaseViewHolder {

        @BindView(R.id.img_gzhp)
        SketchImageView img_gzhp;

        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.img_now)
        SketchImageView img_now;
        @BindView(R.id.layout_lrecord)
        LinearLayout layout_lrecord;


        @BindView(R.id.tv_cjgm)
        TextView tv_cjgm;
        @BindView(R.id.tv_qpj)
        TextView tv_qpj;
        @BindView(R.id.tv_jjfd)
        TextView tv_jjfd;
        @BindView(R.id.tv_sxf)
        TextView tv_sxf;
        @BindView(R.id.tv_jpdjs)
        TextView tv_jpdjs;
        @BindView(R.id.tv_tbbl)
        TextView tv_tbbl;

        @BindView(R.id.img_bg)
        SketchImageView img_bg;

        @BindView(R.id.tv_lucky_name)
        TextView tv_lucky_name;
        @BindView(R.id.tv_lucky_local)
        TextView tv_lucky_local;
        @BindView(R.id.tv_lucky_tips)
        TextView tv_lucky_tips;
        @BindView(R.id.btn_jpxq)
        TextView btn_jpxq;
        @BindView(R.id.tv_tips)
        TextView tv_tips;

        @BindView(R.id.tv_count_down)
        TextView tv_count_down;

        public StatementViewHolder(View itemView) {
            super(itemView);
            tv_buy_count = (TextView) itemView.findViewById(R.id.tv_buy_count);
            tv_time_count = tv_count_down;
        }

        @Override
        public void onBindViewHolder(int position) {

            tv_count_down.setText(FormatUtils.formatCountDown(model.getInfo().getCountdown()));

            if (model.getInfo().getStatus() != 1) {
                tv_count_down.setVisibility(View.GONE);
            } else {
                tv_count_down.setVisibility(View.VISIBLE);

            }

            btn_jpxq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", HttpMethod.BASE_URL + "biddingrules");
                    doActivity(H5Activity.class, bundle);
                }
            });

            img_gzhp.displayResourceImage(R.drawable.ic_gzhp);

            DisplayOptions options = new DisplayOptions();
            options.setImageShaper(new CircleImageShaper());
            options.setLoadingImage(R.drawable.userpic);
            options.setErrorImage(R.drawable.userpic);

            tv_buy_count.setText("累计消费：" + model.getInfo().getPaicount() + "拍币，" + model.getInfo().getGivecount() + "赠币");
            tv_money_count.setText(paicount + givecount + "/" + (model.getInfo().getCount() + outcount));

            img_now.setOptions(options);


            itemView.findViewById(R.id.line_lbid).setVisibility(model.getLbid() == null ? View.GONE : View.VISIBLE);

            if (model.getLbid() != null) {
                img_now.displayImage(model.getLbid().getImg());
            }

            tv_title.setText(model.getInfo().getName());

            tv_tips.setText("最后出价人");

            img_bg.displayResourceImage(R.drawable.ic_pzrbj);
            tv_lucky_name.setText(model.getLbid().getNickname());
            tv_lucky_local.setText("(" + model.getLbid().getLocal() + ")");

            if (model.getInfo().getStatus() == 1) {
                String t1="若无人出价，将以￥" + model.getInfo().getNowprice() + "拍得本商品";
                tv_lucky_tips.setText(TextSpanUtils.createColorText(t1,8,t1.length()-5,getResources().getColor(R.color.theme_color)));
            } else {
                if (CacheData.getInstance().getUserData().getUid().equals(model.getLbid().getUid())) {
                    String t2="恭喜您以￥" + model.getInfo().getNowprice() + "拍得本商品";
                    tv_lucky_tips.setText(TextSpanUtils.createColorText(t2,4,t2.length()-5,getResources().getColor(R.color.theme_color)));
                } else {
                    String t3="恭喜该用户以￥" + model.getInfo().getNowprice() + "拍得本商品";
                    tv_lucky_tips.setText(TextSpanUtils.createColorText(t3,6,t3.length()-5,getResources().getColor(R.color.theme_color)));
                }
            }

            String str_qpj = "起拍价：￥0.0";
            tv_qpj.setText(TextSpanUtils.createColorText(str_qpj, 4, str_qpj.length(), getResources().getColor(R.color.theme_color)));
            String str_jjfd = "加价幅度：￥0.10";
            tv_jjfd.setText(TextSpanUtils.createColorText(str_jjfd, 5, str_jjfd.length(), getResources().getColor(R.color.theme_color)));

            String str_sxf = "手续费：1拍币/次";
            tv_sxf.setText(TextSpanUtils.createColorText(str_sxf, 4, str_sxf.length(), getResources().getColor(R.color.theme_color)));

            String str_jjdjs = "竞拍倒计时：10s";
            tv_jpdjs.setText(TextSpanUtils.createColorText(str_jjdjs, 6, str_jjdjs.length(), getResources().getColor(R.color.theme_color)));

            String str_cjgm = "差价购买：有";
            tv_cjgm.setText(TextSpanUtils.createColorText(str_cjgm, 5, str_cjgm.length(), getResources().getColor(R.color.theme_color)));

            String str_tbbl = "返币比例：100%";
            tv_tbbl.setText(TextSpanUtils.createColorText(str_tbbl, 5, str_tbbl.length(), getResources().getColor(R.color.theme_color)));


            itemView.findViewById(R.id.btn_join_list).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("nowprice", model.getInfo().getNowprice());
                    bundle.putString("aid", model.getInfo().getAid());
                    doActivity(JoinListActivity.class, bundle);
                }
            });


            layout_lrecord.removeAllViews();
            for (int i = 0; i < model.getLrecords().size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.viewholder_detail_join_list_item, null);
                SketchImageView img_type = (SketchImageView) view.findViewById(R.id.img_type);
                TextView tv_nameid = (TextView) view.findViewById(R.id.tv_nameid);
                TextView tv_nowprice = (TextView) view.findViewById(R.id.tv_nowprice);
                TextView tv_local = (TextView) view.findViewById(R.id.tv_local);
                TextView tv_state = (TextView) view.findViewById(R.id.tv_state);

                tv_nameid.setText(model.getLrecords().get(i).getNickname());
                tv_local.setText(model.getLrecords().get(i).getLocal());

                float kk = Float.parseFloat(model.getInfo().getNowprice());

                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String temp = "￥" + decimalFormat.format(kk - 0.1 * i);

                tv_nowprice.setText(temp);
                tv_state.setText("出局");
                img_type.setOptions(options);
                img_type.displayImage(model.getLrecords().get(i).getImg());
                layout_lrecord.addView(view);
            }
        }


        @Override
        public void onBindViewHolder(int position, List<Object> payloads) {
            for (Object payload : payloads) {
                // 刷新倒计时
                if (payload instanceof Integer && ((int) payload) == 2) {
                    tv_nowprice.setText("￥" + model.getInfo().getNowprice());
                    tv_count_down.setText(FormatUtils.formatCountDown(model.getInfo().getCountdown()));
                    startRefreshTime(model.getInfo().getCountdown());
                    //刷新数据吧少年

                    img_now.displayImage(model.getLbid().getImg());
                    //tv_join_count.setText(model.getInfo().getRecordcount() + "条");

                    tv_buy_count.setText("累计消费：" + paicount + "拍币，" + givecount + "赠币");
                    tv_money_count.setText(paicount + givecount + "/" + (model.getInfo().getCount() + outcount));

                    layout_lrecord.removeAllViews();
                    for (int i = 0; i < model.getLrecords().size(); i++) {
                        View view = getLayoutInflater().inflate(R.layout.viewholder_detail_join_list_item, null);
                        SketchImageView img_type = (SketchImageView) view.findViewById(R.id.img_type);
                        TextView tv_nameid = (TextView) view.findViewById(R.id.tv_nameid);
                        TextView tv_nowprice = (TextView) view.findViewById(R.id.tv_nowprice);
                        TextView tv_local = (TextView) view.findViewById(R.id.tv_local);
                        TextView tv_state = (TextView) view.findViewById(R.id.tv_state);

                        tv_nameid.setText(model.getLrecords().get(i).getNickname());
                        tv_local.setText(model.getLrecords().get(i).getLocal());

                        float kk = Float.parseFloat(model.getInfo().getNowprice());

                        DecimalFormat decimalFormat = new DecimalFormat("0.0");
                        String temp = "￥" + decimalFormat.format(kk - 0.1 * (i + 1));

                        tv_nowprice.setText(temp);
                        tv_state.setText("出局");

                        DisplayOptions options = new DisplayOptions();
                        options.setImageShaper(new CircleImageShaper());
                        options.setErrorImage(R.drawable.userpic);
                        options.setLoadingImage(R.drawable.userpic);
                        img_type.setOptions(options);
                        img_type.displayImage(model.getLrecords().get(i).getImg());

                        layout_lrecord.addView(view);
                    }

                }
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class TabViewHolder extends BaseViewHolder {


        public TabViewHolder(View itemView) {
            super(itemView);
            tab_item = itemView;
        }

        @Override
        public void onBindViewHolder(int position) {
            selectTab(tabIndex);
            itemView.findViewById(R.id.btn_sdfx).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab(0);
                }
            });

            itemView.findViewById(R.id.btn_wqcj).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab(1);
                }
            });

            itemView.findViewById(R.id.btn_jpgz).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab(2);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class HistoryViewHolder extends BaseViewHolder {

        @BindView(R.id.img_logo)
        SketchImageView img_logo;
        @BindView(R.id.tv_nameid)
        TextView tv_nameid;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_local)
        TextView tv_local;
        @BindView(R.id.tv_nowprice)
        TextView tv_nowprice;
        @BindView(R.id.tv_free_count)
        TextView tv_free_count;

        public HistoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

            ShopHistoryBean model = (ShopHistoryBean) mDataList.get(position);

            DisplayOptions options = new DisplayOptions();
            options.setImageShaper(new CircleImageShaper());
            options.setErrorImage(R.drawable.userpic);
            options.setLoadingImage(R.drawable.userpic);
            img_logo.setOptions(options);
            img_logo.displayImage(model.getImg());

            tv_nameid.setText("成交人：" + model.getNickname());
            tv_time.setText(model.getCreatetime());
            tv_local.setText("地　区：" + model.getLocal());

            String temp = "成交价：￥" + model.getNowprice();
            tv_nowprice.setText(TextSpanUtils.createColorText(temp, 4, temp.length(), getResources().getColor(R.color.theme_color)));
            tv_free_count.setText(model.getSavemoney());
        }

        @Override
        public void onItemClick(View view, int position) {
            Bundle bundle = new Bundle();
            bundle.putString("id", ((ShopHistoryBean) mDataList.get(position)).getAid());
            bundle.putString("view", "3");
            doActivity(ShopDetailActivity.class, bundle);
        }
    }

    class RuleViewHolder extends BaseViewHolder {


        public RuleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class NoDataViewHolder extends BaseViewHolder {

        @BindView(R.id.img)
        SketchImageView img;
        @BindView(R.id.tv_tips)
        TextView tv_tips;

        public NoDataViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBindViewHolder(int position) {
            img.displayResourceImage(R.drawable.no_date);
            tv_tips.setText("暂无记录");
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class InfoViewHolder extends BaseViewHolder {

        public InfoViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            TextView tv = (TextView) itemView;
            tv.setText("没有更多了");
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    class LuckyShowViewHolder extends BaseViewHolder {

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

        public LuckyShowViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {

            LuckyShowListBean model = (LuckyShowListBean) mDataList.get(position);

            DisplayOptions options = new DisplayOptions();
            options.setImageShaper(new CircleImageShaper());
            options.setLoadingImage(R.drawable.userpic);
            options.setErrorImage(R.drawable.userpic);
            img_logo.displayImage(model.getImg());
            img_logo.setOptions(options);
            tv_time.setText(model.getCreatetime());
            tv_content.setText(model.getContent());
            tv_title.setText(model.getTitle());
            tv_nameid.setText(model.getNickname());

            String img = model.getImages();
            if (!TextUtils.isEmpty(img)) {
                ninegridview.setVisibility(View.VISIBLE);
                final String[] newArryString = model.getImages().split(";");
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < newArryString.length; i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(imgBaseUrl + newArryString[i]);
                    info.setBigImageUrl(imgBaseUrl + newArryString[i]);
                    imageInfo.add(info);
                }
                ninegridview.setAdapter(new NineGridViewClickAdapter(ShopDetailActivity.this, imageInfo));
            } else {
                ninegridview.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }


    public static class MyHanlder extends Handler {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
        unregisterReceiver(myReceiver);
    }

    public void goToLogin() {
        doActivityForResult(LoginActivity.class, null, 100);
        overridePendingTransition(R.anim.login_in_anim, R.anim.activity_nor_anim);
    }

    public void showEditShopNumberDialog() {
        final int max = 5000;
        final int min = 1;
        final Dialog dialog = new Dialog(this, R.style.mydialog);
        dialog.setContentView(R.layout.dialog_numberadd_layout);
        final ShopNumberView shopNumberView = (ShopNumberView) dialog.findViewById(R.id.shop_number_view);
        shopNumberView.setCanEdit(true);
        FlatButton dialog_numberadd_sure = (FlatButton) dialog.findViewById(R.id.dialog_numberadd_sure);
        FlatButton dialog_numberadd_cancle = (FlatButton) dialog.findViewById(R.id.dialog_numberadd_cancle);
        shopNumberView.setMaxValue(5000);
        dialog_numberadd_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog_numberadd_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定
                if (TextUtils.isEmpty(shopNumberView.getEditView().getText().toString())) {
                    shopNumberView.setValue(1);
                    count = 1;
                    tv_join_count.setText(count + "");
                    dialog.dismiss();
                    return;
                }
                count = Integer.parseInt(shopNumberView.getEditView().getText().toString());
                if (count >= max) {
                    count = max;
                } else if (count <= min) {
                    count = min;
                }
                shopNumberView.setValue(count);
                tv_join_count.setText(count + "");
                dialog.dismiss();
            }
        });
        shopNumberView.getEditView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopNumberView.getEditView().selectAll();
            }
        });

        shopNumberView.getEditView().setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //这里可以添加自己的菜单选项（前提是要返回true的）
                return false;//返回false 就是屏蔽ActionMode菜单
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = DisplayUtils.getScreenW() - DisplayUtils.dip2px(this, 40);
        params.height = -2;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        shopNumberView.getEditView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(shopNumberView.getEditView().getText().toString())) {
                        shopNumberView.setValue(1);
                        dialog.dismiss();
                        return true;
                    }
                    int count = Integer.parseInt(shopNumberView.getEditView().getText().toString());
                    if (count >= max) {
                        count = max;
                    } else if (count <= min) {
                        count = min;
                    }
                    shopNumberView.setValue(count);
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        shopNumberView.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(shopNumberView.getEditView(), 0);
                int temp = count;
                shopNumberView.setValue(temp);
                shopNumberView.getEditView().selectAll();
            }
        }, 500);
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ServceMessageBean bean = (ServceMessageBean) intent.getSerializableExtra("model");
            if (model == null) {
                return;
            }
            //开始刷新数啦
            if (bean.getAid().equals(model.getInfo().getAid())) {
                if (bean.getStatus() == 2 || bean.getStatus() == 3) {
                    getData(true, false);
                    return;
                }
                ShopLbidBean temp = new ShopLbidBean();
                List<LrecordBean> tlist = new ArrayList<>();
                LrecordBean lb = new LrecordBean();
                lb.setLocal(model.getLbid().getLocal());
                lb.setNickname(model.getLbid().getNickname());
                lb.setImg(model.getLbid().getImg());
                lb.setNowprice(model.getInfo().getNowprice());
                tlist.add(lb);
                for (int i = 0; i < model.getLrecords().size(); i++) {
                    if (i == model.getLrecords().size() - 1 && model.getLrecords().size() >= 3) {
                        break;
                    }
                    tlist.add(model.getLrecords().get(i));
                }
                temp.setImg(bean.getIconimage());
                temp.setLocal(bean.getAddress());
                temp.setNickname(bean.getLastbid());
                temp.setUid(bean.getLastuid());
                model.setLbid(temp);
                model.setLrecords(tlist);
                model.getInfo().setNowprice(bean.getNowprice());
                model.getInfo().setCountdown(bean.getCountdown());
                model.getInfo().setRecordcount((model.getInfo().getRecordcount() + 1));
                adapter.notifyItemChanged(1, 2);
                adapter.notifyItemChanged(2, 2);
                upPriceLayoutData();
                if (bean.getLastuid().equals(CacheData.getInstance().getUserData().getUid())) {
                    getUserMoney();
                }
            }
        }

    };


    /**
     * 倒计时
     */


    CountDownTimer countDownTimer;
    public  void setTime(long altogetherTime, long spacingTime) {
        if (countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
        countDownTimer=new CountDownTimer(altogetherTime, spacingTime) {

            @Override
            public void onTick(long millisUntilFinished) {
                long sec=millisUntilFinished/1000;//秒
                long mill=millisUntilFinished-sec*1000;//毫秒
                String temp="";
                if (mill>=100){
                    temp=(mill+"").substring(0,2);
                }else if(mill>=10){
                    temp=mill+"";
                }else {
                    temp="0"+mill;
                }
                if (tv_time_count!=null)
                 tv_time_count.setText("00:0"+sec+":"+temp);
            }

            @Override
            public void onFinish() {
                if (tv_time_count!=null)
                    tv_time_count.setText("00:00:00");
            }
        }.start();
    }


}
