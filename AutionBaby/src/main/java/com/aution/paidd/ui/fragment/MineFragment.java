package com.aution.paidd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.framework.core.base.BaseFragment;
import com.framework.core.utils.LogUtils;
import com.framework.core.widget.FlatButton;
import com.framework.core.widget.RowCell;
import com.framework.core.widget.RowCellGroupView;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.OnShareCallBack;
import com.aution.paidd.model.ChannelEntity;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.HeadLineResponse;
import com.aution.paidd.response.LoginResponse;
import com.aution.paidd.ui.activity.AdressListActivity;
import com.aution.paidd.ui.activity.H5Activity;
import com.aution.paidd.ui.activity.LoginActivity;
import com.aution.paidd.ui.activity.LuckyShowActivity;
import com.aution.paidd.ui.activity.MyScoreActivity;
import com.aution.paidd.ui.activity.PayCenterActivity;
import com.aution.paidd.ui.activity.RecordListActivity;
import com.aution.paidd.ui.activity.SettingActivity;
import com.aution.paidd.ui.activity.ShopCoinListActivity;
import com.aution.paidd.ui.activity.ShopDetailActivity;
import com.aution.paidd.ui.activity.UserInfoActivity;
import com.aution.paidd.ui.adapter.HeaderChannelAdapter;
import com.aution.paidd.ui.widget.FixedGridView;
import com.aution.paidd.ui.widget.MarqueeView;
import com.aution.paidd.utils.ShareUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/30.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.rowcell)
    RowCellGroupView rowcell;

    @BindView(R.id.fix_gridview)
    FixedGridView fix_gridview;

    @BindView(R.id.img)
    SketchImageView img;
    @BindView(R.id.tv_name_id)
    TextView tv_name_id;

    @BindView(R.id.img_set)
    SketchImageView img_set;

    @BindView(R.id.view_scolltext)
    MarqueeView view_scolltext;

    @BindView(R.id.tv_aution_count)
    TextView tv_aution_count;
    @BindView(R.id.tv_system_count)
    TextView tv_system_count;
    @BindView(R.id.tv_free_count)
    TextView tv_free_count;
    @BindView(R.id.tv_score)
    TextView tv_score;
    @BindView(R.id.btn_paycenter)
    FlatButton btn_paycenter;


    HeadLineResponse headLineResponse;

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initValue() {
        List<RowCell> arr = new ArrayList<>();
        arr.add(new RowCell().setViewid(6).setLabel("我的晒单").setResid(R.drawable.ic_wdsd));
        arr.add(new RowCell().setViewid(0).setLabel("收货信息").setResid(R.drawable.ic_shxx));
        arr.add(new RowCell().setViewid(1).setLabel("新手指南").setResid(R.drawable.ic_xszn));
        arr.add(new RowCell().setViewid(3).setLabel("分享应用").setResid(R.drawable.ic_new_share));
        arr.add(new RowCell().setViewid(2).setLabel("客服中心").setResid(R.drawable.ic_kfzx));
        arr.add(null);
        rowcell.notifyDataChanged(arr);

        List<ChannelEntity> list = new ArrayList<>();
        list.add(new ChannelEntity("正在拍", R.drawable.ic_zzp));
        list.add(new ChannelEntity("我拍中", R.drawable.ic_wpz));
        list.add(new ChannelEntity("差价购", R.drawable.ic_new_cjg));
        list.add(new ChannelEntity("待付款", R.drawable.ic_dfk));
        list.add(new ChannelEntity("待签收", R.drawable.ic_dqs));
        fix_gridview.setAdapter(new HeaderChannelAdapter(getContext(), list));
        fix_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!CacheData.getInstance().isLogin()) {
                    goToLogin();
                    return;
                }

                Bundle bundle = new Bundle();
                switch (i) {
                    case 0:
                        bundle.putInt("index", 0);
                        doActivity(RecordListActivity.class, bundle);
                        break;
                    case 1:
                        bundle.putInt("index", 1);
                        doActivity(RecordListActivity.class, bundle);
                        break;
                    case 2:
                        bundle.putInt("index", 2);
                        doActivity(RecordListActivity.class, bundle);
                        break;
                    case 3:
                        bundle.putInt("index", 3);
                        doActivity(RecordListActivity.class, bundle);
                        break;
                    case 4:
                        bundle.putInt("index", 4);
                        doActivity(RecordListActivity.class, bundle);
                        break;
                }
            }
        });

        showLoginState(CacheData.getInstance().isLogin());
        img_set.displayResourceImage(R.drawable.ic_set);

        img_set.setOnClickListener(this);
        root.findViewById(R.id.btn_user_info).setOnClickListener(this);
        root.findViewById(R.id.btn_paycenter).setOnClickListener(this);
        root.findViewById(R.id.btn_gb).setOnClickListener(this);
        root.findViewById(R.id.btn_pb).setOnClickListener(this);
        root.findViewById(R.id.btn_zb).setOnClickListener(this);
        root.findViewById(R.id.btn_jf).setOnClickListener(this);

        rowcell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case 0:
                        if (CacheData.getInstance().isLogin()) {
                            doActivity(AdressListActivity.class);
                        } else {
                            goToLogin();
                        }
                        break;
                    case 1: {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", HttpMethod.BASE_URL + "newhelp");
                        doActivity(H5Activity.class, bundle);
                    }
                    break;
                    case 2: {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", HttpMethod.BASE_URL + "question");
                        doActivity(H5Activity.class, bundle);
                    }
                    break;
                    case 3:
                        ShareUtils.showAllShare(getContext(), "拍我所爱，竞在其中！", null, "一个收获惊喜多多的地方，从这里发现惊喜并实现更多心愿的地方！速来拍多多实现您的愿望吧！", null, R.drawable.icon, new OnShareCallBack() {
                            @Override
                            public void onSuccess() {
                                //分享成功
                                shareOk();
                            }
                        });
                        break;
                    case 6:
                        if (CacheData.getInstance().isLogin()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", CacheData.getInstance().getUserData().getUid());
                            doActivity(LuckyShowActivity.class, bundle);
                        } else {
                            goToLogin();
                        }
                        break;
                }
            }
        });

        getHeadLine();

        view_scolltext.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Bundle bundle = new Bundle();
                bundle.putString("id", headLineResponse.getObj().get(position).getCid());
                bundle.putString("view", "2");
                doActivity(ShopDetailActivity.class, bundle);
            }
        });
    }


    public void shareOk(){
        Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                if (baseResponse!=null){
                    showToast(baseResponse.getMsg());
                    getUserInfo();
                }
            }
        };
        BaseIdRequest request=new BaseIdRequest();
        request.setUid(CacheData.getInstance().getUserData().getUid());
        HttpMethod.getInstance().getShareOk(subscriber,request);
    }


    public void upLoginDataUI() {
        UserBean model = CacheData.getInstance().getUserData();
        DisplayOptions options = new DisplayOptions();
        options.setImageShaper(new CircleImageShaper());
        options.setLoadingImage(R.drawable.userpic);
        options.setErrorImage(R.drawable.userpic);
        img.setOptions(options);

        tv_aution_count.setText(model.getPatmoney() + "");
        tv_system_count.setText(model.getGivemoney() + "");
        tv_free_count.setText(model.getBuymoney() + "");
        tv_score.setText(model.getExperience() + "");

        img.displayImage(model.getImg());
        tv_name_id.setVisibility(View.VISIBLE);
        tv_name_id.setText(model.getNickname() + "(ID：" + model.getUid() + ")");
        btn_paycenter.setText("充值");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                doActivity(SettingActivity.class);
                break;
            case R.id.btn_user_info:
                if (CacheData.getInstance().isLogin()) {
                    doActivityForresult(100, UserInfoActivity.class);
                } else {
                    goToLogin();
                }
                break;
            case R.id.btn_paycenter:
                if (CacheData.getInstance().isLogin()) {
                    doActivity(PayCenterActivity.class);
                } else {
                    goToLogin();
                }
                break;
            case R.id.btn_pb: {

                if (CacheData.getInstance().isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);//
                    doActivity(ShopCoinListActivity.class, bundle);
                } else {
                    goToLogin();
                }
            }
            break;
            case R.id.btn_zb: {

                if (CacheData.getInstance().isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    doActivity(ShopCoinListActivity.class, bundle);
                } else {
                    goToLogin();
                }


            }
            break;
            case R.id.btn_gb:

                if (CacheData.getInstance().isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 3);//
                    doActivity(ShopCoinListActivity.class, bundle);
                } else {
                    goToLogin();
                }


                break;
            case R.id.btn_jf:
                if (CacheData.getInstance().isLogin()) {
                    doActivityForresult(100, MyScoreActivity.class);
                } else {
                    goToLogin();
                }
                break;
        }
    }


    public void getHeadLine() {
        Subscriber<HeadLineResponse> subscriber = new Subscriber<HeadLineResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HeadLineResponse response) {
                headLineResponse = response;
                if (headLineResponse != null) {

                    if (headLineResponse.getCode() == 10000 && headLineResponse.getObj() != null && headLineResponse.getObj().size() > 0) {
                        //刷新
                        if (view_scolltext != null) {
                            view_scolltext.setNotices(headLineResponse.getObj());
                            view_scolltext.start();
                        }
                    }
                } else {
                    showToast(Constant.Service_Err);
                }
            }
        };
        HttpMethod.getInstance().getHeadLine(subscriber);
    }


    public void getUserInfo() {
        if (!CacheData.getInstance().isLogin()) {
            //去登录
            showLoginState(false);
            return;
        }
        BaseIdRequest request = new BaseIdRequest();
        request.setUid(CacheData.getInstance().getUserData().getUid());
        Subscriber<LoginResponse> subscriber = new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginResponse response) {
                if (response != null && response.getObj() != null) {
                    CacheData.getInstance().cacheUserData(response.getObj());
                    upLoginDataUI();
                }
            }
        };
        HttpMethod.getInstance().getUserInfo(subscriber, request);
    }


    /**
     * 改变登录状态
     *
     * @param isLogin
     */
    public void showLoginState(boolean isLogin) {
        if (img == null) {
            return;
        }
        if (isLogin) {
            upLoginDataUI();
        } else {
            DisplayOptions options = new DisplayOptions();
            options.setImageShaper(new CircleImageShaper());
            img.setOptions(options);
            img.displayResourceImage(R.drawable.userpic);
            tv_aution_count.setText("0");
            tv_system_count.setText("0");
            tv_free_count.setText("0");
            tv_score.setText("0");
            tv_name_id.setVisibility(View.GONE);
            btn_paycenter.setText("登录/注册");
        }
    }

    @Override
    protected void onFirstUserVisible() {
        LogUtils.e("onFirstUserVisible");
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getUserInfo();
    }

    public void goToLogin() {
        startActivityForResult(new Intent(getContext(), LoginActivity.class), 100);
        getActivity().overridePendingTransition(R.anim.login_in_anim, R.anim.activity_nor_anim);
    }
}
